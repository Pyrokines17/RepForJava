package server;

import java.io.*;
import java.nio.*;
import java.sql.*;
import java.util.*;
import java.nio.channels.*;
import java.util.logging.*;

public class ServerMain {

    public static void main (String[] args) throws IOException {
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

        SerCommandManager serCommandManager = new SerCommandManager(connectionWithPostgres);
        ByteBuffer bufForMes;

        //noinspection InfiniteLoopStatement

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
                        if (serCommandManager.parse(bufForMes)) {
                            serCommandManager.sendSuccess(key);
                        } else {
                            serCommandManager.sendError(key);
                        }
                    }
                }

                keyIterator.remove();
            }
        }
    }

}
