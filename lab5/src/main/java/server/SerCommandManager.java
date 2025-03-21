package server;

import java.io.*;
import java.nio.*;
import java.sql.*;
import java.util.*;

import server.sql.*;
import xml.XMLCreate;
import xml.commands.*;
import java.nio.file.*;
import jakarta.xml.bind.*;

import java.nio.channels.*;
import java.util.logging.*;
import java.util.concurrent.*;

import xml.commands.files.*;
import static server.sql.SQLConst.*;
import org.apache.commons.collections4.queue.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class SerCommandManager {
    private final SQLWorker sqlWorker;
    private final XMLCreate xmlCreate;
    private final SerEventManager serEventManager;
    private final Connection connectionWithPostgres;
    private final Queue<ConnectionOfMessages> bufferOfStr;

    private final ConcurrentMap<SelectionKey, Login> activeUsers = new ConcurrentHashMap<>();
    private final ConcurrentMap<SelectionKey, Long> lastHeartbeat = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Description> profiles = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> files = new ConcurrentHashMap<>();
    private final static XMLInputFactory factory = XMLInputFactory.newInstance();

    private static final String AVATAR = "src/main/java/server/avatars/";
    private static final String BASE = "src/main/java/server/files/";
    private static final int BUFFER_SIZE = 10;

    public SerCommandManager(Connection connectionWithPostgres) {
        this.connectionWithPostgres = connectionWithPostgres;
        this.bufferOfStr = new CircularFifoQueue<>(BUFFER_SIZE);
        this.serEventManager = new SerEventManager();
        this.xmlCreate = new XMLCreate();
        this.sqlWorker = new SQLWorker();
    }

    public SerEventManager getSerEventManager() {
        return serEventManager;
    }

    public void parse(ByteBuffer bufForMes, SelectionKey key) throws IOException {
        String name;

        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(bufForMes.array());
            XMLEventReader reader = factory.createXMLEventReader(new InputStreamReader(stream));
            XMLEvent xmlEvent = reader.nextEvent();

            while (!xmlEvent.isStartElement()) {
                xmlEvent = reader.nextEvent();
            }

            StartElement startElement = xmlEvent.asStartElement();

            Iterator<Attribute> iterator = startElement.getAttributes();
            Attribute attribute = iterator.next();
            name = attribute.getValue();
        } catch (XMLStreamException e) {
            serEventManager.setError(e.getLocalizedMessage());
            serEventManager.sendError(key);
            return;
        }

        switch (name) {
            case "login":
                if (key.attachment() == null && login(bufForMes, key)) {
                    serEventManager.sendSuccess(key);
                    serEventManager.sendMessages(bufferOfStr, key);
                } else {
                    if (key.attachment() != null ) {
                        serEventManager.setError("User already logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "list":
                if (key.attachment() != null && list()) {
                    serEventManager.sendListSuccess(key, activeUsers);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "message":
                if (key.attachment() != null && message(bufForMes, key)) {
                    serEventManager.sendSuccess(key);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "logout":
                if (key.attachment() != null && logout(key)) {
                    serEventManager.sendSuccess(key);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "upload":
                String uuid;
                if (key.attachment() != null && (uuid = upload(bufForMes, key)) != null) {
                    serEventManager.sendFileSuccess(key, uuid);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "download":
                String fileUuid;
                if (key.attachment() != null && (fileUuid = download(bufForMes)) != null) {
                    serEventManager.sendDownload(key, fileUuid, files);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "filelist":
                if (key.attachment() != null && filelist()) {
                    serEventManager.sendFileList(key, files);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "showprofile":
                String username;
                if (key.attachment() != null && (username = showProfile(bufForMes)) != null) {
                    serEventManager.sendProfile(key, username, profiles.get(username));
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "saveprofile":
                if (key.attachment() != null && saveProfile(bufForMes, key)) {
                    serEventManager.sendSuccess(key);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    }

                    serEventManager.sendError(key);
                }
                break;
            default:
                serEventManager.setError("Unknown command");
                serEventManager.sendError(key);
                break;
        }
    }

    private boolean saveProfile(ByteBuffer bufForMes, SelectionKey key) {
        try {
            JAXBContext context = JAXBContext.newInstance(SaveProfile.class);
            SaveProfile saveProfile = (SaveProfile) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));

            try {
                if (profiles.containsKey(saveProfile.getUsername())) {
                    Path oldAva = Paths.get(AVATAR, profiles.get(saveProfile.getUsername()).getAvaPath());
                    Files.delete(oldAva);
                    Logger.getGlobal().info("Avatar " + profiles.get(saveProfile.getUsername()).getAvaPath() + " deleted");
                    profiles.remove(saveProfile.getUsername());
                }

                Path newAva = Paths.get(AVATAR, saveProfile.getFilename());
                Files.createFile(newAva);
                Logger.getGlobal().info("Avatar " + saveProfile.getFilename() + " created");

                byte[] decodedContent = Base64.getDecoder().decode(saveProfile.getContent());
                Files.write(newAva, decodedContent);
                Logger.getGlobal().info("Avatar " + saveProfile.getFilename() + " uploaded");

                profiles.put(saveProfile.getUsername(), new Description(saveProfile.getStatus(), saveProfile.getFilename()));
            } catch (IOException e) {
                serEventManager.setError(e.getLocalizedMessage());
                return false;
            }

            return true;
        } catch (JAXBException e) {
            serEventManager.setError(e.getLocalizedMessage());
            return false;
        }
    }

    private String showProfile(ByteBuffer bufForMes) {
        try {
            JAXBContext context = JAXBContext.newInstance(ShowProfile.class);
            ShowProfile showProfile = (ShowProfile) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));

            boolean cont = profiles.containsKey(showProfile.getUsername());

            if (!cont) {
                serEventManager.setError("User haven't profile");
                return null;
            }

            return showProfile.getUsername();
        } catch (JAXBException e) {
            serEventManager.setError(e.getLocalizedMessage());
            return null;
        }
    }

    private boolean filelist() {
        Logger.getGlobal().info("List of files");
        return true;
    }

    private String download(ByteBuffer bufForMes) {
        try {
            JAXBContext context = JAXBContext.newInstance(Download.class);
            Download download = (Download) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));

            return download.getId();
        } catch (JAXBException e) {
            serEventManager.setError(e.getLocalizedMessage());
            return null;
        }
    }

    private String upload(ByteBuffer bufForMes, SelectionKey key) {
        try {
            JAXBContext context = JAXBContext.newInstance(Upload.class);
            Upload upload = (Upload) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));

            Path newFile = Paths.get(BASE, upload.getFileName());
            Files.createFile(newFile);
            Logger.getGlobal().info("File " + upload.getFileName() + " created");

            byte[] decodedContent = Base64.getDecoder().decode(upload.getContent());
            Files.write(newFile, decodedContent);
            Logger.getGlobal().info("File " + upload.getFileName() + " uploaded");

            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString().replace("-", "");
            files.put(uuidString, upload.getFileName());
            long size = Files.size(newFile);

            serEventManager.broadCast(
                    xmlCreate.getNewFile(uuidString, upload, key, size), activeUsers);

            return uuidString;
        } catch (JAXBException | IOException e) {
            serEventManager.setError(e.getLocalizedMessage());
            return null;
        }
    }

    private boolean login(ByteBuffer bufForMes, SelectionKey key) {
        try {
            JAXBContext context = JAXBContext.newInstance(Login.class);
            Login login = (Login) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));

            PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(getInsertUsersSql());
            PreparedStatement checkUser = connectionWithPostgres.prepareStatement(getCheckUserSql());

            if (sqlWorker.checkUser(checkUser, login)) {
                String storedPassword = sqlWorker.getPassword(connectionWithPostgres, login);

                if (!sqlWorker.getPasswordEncoder().matches(login.getPassword(), storedPassword)) {
                    serEventManager.setError("Wrong password");
                    return false;
                }
            } else {
                sqlWorker.addUser(preparedStatement, login, connectionWithPostgres);
            }

            key.attach(login);
            activeUsers.put(key, login);
            serEventManager.broadCast(xmlCreate.getUserlogin(login.getUsername()), activeUsers);
            Logger.getGlobal().info("User " + login.getUsername() + " logged in");

            return true;
        } catch (SQLException | JAXBException e) {
            serEventManager.setError(e.getLocalizedMessage());
            return false;
        }
    }

    private boolean list() {
        Logger.getGlobal().info("List of users");
        return true;
    }

    private boolean message(ByteBuffer bufForMes, SelectionKey key) {
        try {
            Login login = activeUsers.get(key);
            JAXBContext context = JAXBContext.newInstance(ClientMes.class);
            ClientMes clientMes = (ClientMes)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));
            serEventManager.broadCast(xmlCreate.getServerMes(login.getUsername(), clientMes.getMessage()), activeUsers);
            bufferOfStr.add(new ConnectionOfMessages(clientMes.getMessage(), login.getUsername()));

            return true;
        } catch (JAXBException e) {
            serEventManager.setError(e.getLocalizedMessage());
            return false;
        }
    }

    public boolean logout(SelectionKey key) {
        Login login = activeUsers.get(key);

        if (login == null) {
            serEventManager.setError("User not logged in");
            return false;
        }

        try {
            PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(getUpdateLastLogin());
            sqlWorker.updateLastLogin(preparedStatement, login);
        } catch (SQLException e) {
            serEventManager.setError(e.getLocalizedMessage());
            return false;
        }

        key.attach(null);
        serEventManager.broadCast(xmlCreate.getUserlogout(login.getUsername()), activeUsers);
        Logger.getGlobal().info("User " + activeUsers.get(key).getUsername() + " logged out");

        activeUsers.remove(key);
        lastHeartbeat.remove(key);

        return true;
    }

    public ConcurrentMap<SelectionKey, Long> getLastHeartbeat() {
        return lastHeartbeat;
    }
}
