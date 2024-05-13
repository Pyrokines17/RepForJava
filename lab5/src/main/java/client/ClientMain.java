package client;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        String hostname;
        int port;

        if (args.length != 0 && args[0].equals("def")) {
            hostname = "localhost";
            port = 8008;
        } else if (args.length == 2) {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            throw new IllegalArgumentException("Usage: java -cp target/class client.ClientMain <hostname> <port>");
        }

        socketChannel.connect(new InetSocketAddress(hostname, port));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        CliCommandManager commandManager = new CliCommandManager(socketChannel);

        String[] parts;
        String line;

        do {
            line = reader.readLine();
            parts = line.split("-");

            switch (parts[0]) {
                case "list":
                    commandManager.list();
                    break;
                case "logout":
                    commandManager.logout();
                    break;
                case "clientMes":
                    commandManager.clientMes(parts[1]);
                    break;
                case "login":
                    commandManager.login(parts[1], parts[2]);
                    break;
                default:
                    break;
            }

        } while (!parts[0].equals("exit"));

        socketChannel.close();
    }
}
