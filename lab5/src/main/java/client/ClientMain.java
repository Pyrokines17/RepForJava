package client;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class ClientMain { //ToDo: do GUI (and maybe norm end of thread)
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

        Listener listener = new Listener(socketChannel);
        listener.start();

        String[] parts;
        String line;

        do {
            line = reader.readLine();
            parts = line.split("-");

            try {
                if (parts.length != 0) {
                    switch (parts[0]) {
                        case "list":
                            commandManager.list();
                            break;
                        case "logout":
                            commandManager.logout();
                            break;
                        case "clientMes":
                            if (parts.length != 2) {
                                throw new IllegalArgumentException("Usage: clientMes-<message>");
                            }
                            commandManager.clientMes(parts[1]);
                            break;
                        case "login":
                            if (parts.length != 3) {
                                throw new IllegalArgumentException("Usage: login-<username>-<password>");
                            }
                            commandManager.login(parts[1], parts[2]);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getLocalizedMessage());
            }

        } while (!parts[0].equals("exit"));

        listener.stopListener();
        socketChannel.close();
    }
}
