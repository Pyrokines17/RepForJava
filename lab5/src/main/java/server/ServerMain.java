package server;

import jakarta.xml.bind.JAXBContext;

import jakarta.xml.bind.JAXBException;
import xml.*;
import java.io.*;
import java.nio.*;
import java.sql.*;
import java.util.*;
import java.nio.channels.*;
import java.util.logging.*;

public class ServerMain {

    public static void main (String[] args) throws IOException, JAXBException, SQLException {
        PostgresHandler postgresHandler = new PostgresHandler();
        ServerSettings serverSettings = new ServerSettings();
        ServerPreparer serverPreparer = new ServerPreparer();

        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java -cp target/class server.ServerMain <config file>");
        }

        serverPreparer.parseConfig(args[0], postgresHandler, serverSettings);

        ServerSocketChannel serverSocketChannel;
        Connection connectionWithPostgres;
        Selector selector;

        if (serverSettings.getLogging()) {
            Logger.getGlobal().setLevel(Level.INFO);
        } else {
            Logger.getGlobal().setLevel(Level.OFF);
        }

        try {
            connectionWithPostgres = postgresHandler.setConnection();
            Logger.getGlobal().info("Connected to Postgres");

            serverSocketChannel = ServerSocketChannel.open();
            serverPreparer.configureServer(serverSocketChannel, serverSettings);
            Logger.getGlobal().info("Server configured");

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            Logger.getGlobal().info("Server started");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        String INSERT_USERS_SQL = "INSERT INTO accounts" +
                "  (user_id, username, password, email, created_at, last_login) VALUES " +
                " (?, ?, ?, ?, ?, ?);";

        ByteBuffer bufForMes;

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    serverPreparer.acceptConnection(key, selector);
                } else if (key.isReadable()) {
                    bufForMes = serverPreparer.readFromClient(key);

                    if (bufForMes != null) {
                        JAXBContext context = JAXBContext.newInstance(Login.class);
                        Login login = (Login)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));
                        PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(INSERT_USERS_SQL);

                        preparedStatement.setInt(1, 1);
                        preparedStatement.setString(2, login.getUsername());
                        preparedStatement.setString(3, login.getPassword());
                        preparedStatement.setString(4, "teste1@teste.com");
                        preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

                        System.out.println(preparedStatement);
                        preparedStatement.executeUpdate();
                    }
                }

                keyIterator.remove();
            }
        }
    }

}
