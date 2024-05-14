package server;

import java.io.*;
import java.nio.*;
import java.sql.*;

import server.sql.*;
import xml.commands.*;
import jakarta.xml.bind.*;

import java.nio.charset.*;
import java.nio.channels.*;
import java.util.logging.*;
import java.util.concurrent.*;
import static server.sql.SQLConst.*;

public class SerCommandManager {
    private final SQLWorker sqlWorker;
    private final SerEventManager serEventManager;
    private final Connection connectionWithPostgres;

    private final ConcurrentHashMap<SelectionKey, Login> activeUsers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<SelectionKey, Long> lastHeartbeat = new ConcurrentHashMap<>();

    public SerCommandManager(Connection connectionWithPostgres) {
        this.connectionWithPostgres = connectionWithPostgres;
        this.serEventManager = new SerEventManager();
        this.sqlWorker = new SQLWorker();
    }

    public void parse(ByteBuffer bufForMes, SelectionKey key) throws IOException {
        String xmlString = new String(bufForMes.array(), Charset.defaultCharset());
        String[] parts = xmlString.split("\n");
        String name = parts[1].split("\"")[1];

        switch (name) {
            case "login":
                if (key.attachment() == null && login(bufForMes, key)) {
                    serEventManager.sendSuccess(key);
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
                    } else {
                        serEventManager.setError("List failed");
                    }

                    serEventManager.sendError(key);
                }
                break;
            case "message":
                if (key.attachment() != null && message(bufForMes)) {
                    serEventManager.sendSuccess(key);
                } else {
                    if (key.attachment() == null) {
                        serEventManager.setError("User not logged in");
                    } else {
                        serEventManager.setError("Message failed");
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
                    } else {
                        serEventManager.setError("Logout failed");
                    }

                    serEventManager.sendError(key);
                }
                break;
            default:
                break;
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
            Logger.getGlobal().info("User " + login.getUsername() + " logged in");

            return true;
        } catch (SQLException | JAXBException e) {
            e.getLocalizedMessage();
            return false;
        }
    }

    private boolean list() {
        Logger.getGlobal().info("List of users");
        return true;
    }

    private boolean message(ByteBuffer bufForMes) {
        try {
            JAXBContext context = JAXBContext.newInstance(ClientMes.class);
            ClientMes clientMes = (ClientMes)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));
            System.out.println(clientMes.getMessage());

            return true;
        } catch (JAXBException e) {
            e.getLocalizedMessage();
            return false;
        }
    }

    public boolean logout(SelectionKey key) {
        Login login = activeUsers.get(key);

        if (login == null) {
            return false;
        }

        try {
            PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(getUpdateLastLogin());
            sqlWorker.updateLastLogin(preparedStatement, login);
        } catch (SQLException e) {
            e.getLocalizedMessage();
        }

        key.attach(null);
        Logger.getGlobal().info("User " + activeUsers.get(key).getUsername() + " logged out");

        activeUsers.remove(key);
        lastHeartbeat.remove(key);

        return true;
    }

    public ConcurrentHashMap<SelectionKey, Long> getLastHeartbeat() {
        return lastHeartbeat;
    }
}
