package server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

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

    public void configureServer(ServerSocketChannel serverSocketChannel, ServerSettings serverSettings)
            throws IOException {

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(serverSettings.getIp(), serverSettings.getPort()));
    }

    public void acceptConnection(SelectionKey key, Selector selector)
            throws IOException {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public ByteBuffer readFromClient(SelectionKey key)
            throws IOException {

        byte[] bytes = new byte[4];
        SocketChannel socketChannel = (SocketChannel)key.channel();
        int bytesRead = socketChannel.read(ByteBuffer.wrap(bytes));

        if (bytesRead == -1) {
            socketChannel.close();
            key.cancel();
            return null;
        }

        ByteBuffer bufForMes = ByteBuffer.allocate(ByteBuffer.wrap(bytes).getInt());
        int bytesReadForMes = socketChannel.read(bufForMes);

        if (bytesReadForMes == -1) {
            socketChannel.close();
            key.cancel();
            return null;
        }

        bufForMes.flip();
        return bufForMes;
    }
}
