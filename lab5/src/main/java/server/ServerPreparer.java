package server;

import java.io.*;
import java.net.*;
import java.nio.*;
import server.sql.*;
import java.nio.channels.*;
import java.util.logging.Logger;

public class ServerPreparer {

    public void parseConfig(String nameOfConfig, PostgresHandler postgresHandler, ServerSettings serverSettings)
            throws IOException {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nameOfConfig))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] config = line.split("=");
                String[] subNames = config[0].split("_");

                switch (subNames[0]) {
                    case "POSTGRES":
                        switch (subNames[1]) {
                            case "IP":
                                postgresHandler.setIp(config[1]);
                                break;
                            case "PORT":
                                postgresHandler.setPort(config[1]);
                                break;
                            case "NAME":
                                postgresHandler.setName(config[1]);
                                break;
                            case "USERNAME":
                                postgresHandler.setUsername(config[1]);
                                break;
                            case "PASSWORD":
                                postgresHandler.setPassword(config[1]);
                                break;
                        }
                        break;

                    case "SERVER":
                        switch (subNames[1]) {
                            case "IP":
                                serverSettings.setIp(config[1]);
                                break;
                            case "PORT":
                                serverSettings.setPort(config[1]);
                                break;
                            case "LOGGING":
                                serverSettings.setLogging(config[1]);
                                break;
                        }
                        break;
                }
            }
        }
    }

    public void configureServer(ServerSocketChannel serverSocketChannel, ServerSettings serverSettings, int port)
            throws IOException {

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(serverSettings.getIp(), port));
    }

    public void acceptConnection(SelectionKey key, Selector selector)
            throws IOException {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public ByteBuffer readFromClient(SelectionKey key, SerEventManager serEventManager)
            throws IOException {

        ByteBuffer lenMes = ByteBuffer.allocate(4);
        SocketChannel socketChannel = (SocketChannel)key.channel();

        while (lenMes.hasRemaining()) {
            socketChannel.read(lenMes);
        } lenMes.flip();

        int length = lenMes.getInt();

        if (11 * 1024 * 1024 < length) {
            serEventManager.setError("Message is too big");
            serEventManager.sendError(key);
            socketChannel.close();
            return null;
        }

        Logger.getGlobal().info("Length of message will read: " + length);
        ByteBuffer bufForMes = ByteBuffer.allocate(length);

        while (bufForMes.hasRemaining()) {
            socketChannel.read(bufForMes);
        } bufForMes.flip();

        return bufForMes;
    }

}
