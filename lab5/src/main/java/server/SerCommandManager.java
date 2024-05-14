package server;

import java.io.*;
import java.nio.*;
import java.sql.*;
import java.nio.charset.*;
import java.nio.channels.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import xml.*;
import server.sql.*;
import xml.commands.*;
import xml.events.list.*;
import jakarta.xml.bind.*;

public class SerCommandManager {
    private final Connection connectionWithPostgres;
    private final XMLCreate xmlCreate;
    private String error = null;

    private final static String CHECK_USER_SQL = "SELECT COUNT(*) FROM accounts WHERE username = ?;";
    private final static String INSERT_USERS_SQL = "INSERT INTO accounts" +
            "  (user_id, username, password, email, created_at, last_login) VALUES " +
            " (?, ?, ?, ?, ?, ?);";

    private final ConcurrentHashMap<SelectionKey, Login> activeUsers = new ConcurrentHashMap<>();

    public SerCommandManager(Connection connectionWithPostgres) {
        this.connectionWithPostgres = connectionWithPostgres;
        this.xmlCreate = new XMLCreate();
    }

    public void parse(ByteBuffer bufForMes, SelectionKey key) throws IOException {
        String xmlString = new String(bufForMes.array(), Charset.defaultCharset());
        String[] parts = xmlString.split("\n");
        String name = parts[1].split("\"")[1];

        switch (name) {
            case "login":
                if (key.attachment() == null && login(bufForMes, key)) {
                    sendSuccess(key);
                } else {
                    if (key.attachment() != null ) {
                        error = "User already logged in";
                    }

                    sendError(key);
                }
                break;
            case "list":
                if (key.attachment() != null && list()) {
                    sendListSuccess(key);
                } else {
                    if (key.attachment() == null) {
                        error = "User not logged in";
                    } else {
                        error = "List failed";
                    }

                    sendError(key);
                }
                break;
            case "message":
                if (key.attachment() != null && message(bufForMes)) {
                    sendSuccess(key);
                } else {
                    if (key.attachment() == null) {
                        error = "User not logged in";
                    } else {
                        error = "Message failed";
                    }

                    sendError(key);
                }
                break;
            case "logout":
                if (key.attachment() != null && logout(key)) {
                    sendSuccess(key);
                } else {
                    if (key.attachment() == null) {
                        error = "User not logged in";
                    } else {
                        error = "Logout failed";
                    }

                    sendError(key);
                }
                break;
            default:
                break;
        }
    }

    private boolean login(ByteBuffer bufForMes, SelectionKey key) {
        try {
            SQLWorker sqlWorker = new SQLWorker();
            JAXBContext context = JAXBContext.newInstance(Login.class);
            Login login = (Login) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));

            PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(INSERT_USERS_SQL);
            PreparedStatement checkUser = connectionWithPostgres.prepareStatement(CHECK_USER_SQL);

            if (sqlWorker.checkUser(checkUser, login)) {
                String storedPassword = sqlWorker.getPassword(connectionWithPostgres, login);
                if (!sqlWorker.getPasswordEncoder().matches(login.getPassword(), storedPassword)) {
                    error = "Wrong password";
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

    private boolean logout(SelectionKey key) {
        key.attach(null);
        Logger.getGlobal().info("User " + activeUsers.get(key).getUsername() + " logged out");
        activeUsers.remove(key);
        return true;
    }

    public void sendSuccess(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        String xmlString = xmlCreate.getSuccess();
        writeAnswer(xmlString, socketChannel);
    }

    public void sendListSuccess(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();

        ListUsers listUsers = new ListUsers();

        for (Login login : activeUsers.values()) {
            listUsers.addUser(login);
        }

        String xmlString = xmlCreate.getListSuccess(listUsers);
        writeAnswer(xmlString, socketChannel);
    }

    public void sendError(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        String xmlString = xmlCreate.getError(error);
        writeAnswer(xmlString, socketChannel);
    }

    private void writeAnswer(String xmlString, SocketChannel socketChannel)
            throws IOException {

        int len = 4 + xmlString.getBytes().length;
        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.clear();
        buffer.putInt(xmlString.length());
        buffer.put(xmlString.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
