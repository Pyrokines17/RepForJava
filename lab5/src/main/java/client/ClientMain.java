package client;

import controller.*;
import view.Window;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.channels.*;

public class ClientMain {
    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        Listener listener = null;
        Window window = null;

        Controller controller;
        boolean isWin = false;

        try {
            socketChannel = SocketChannel.open();
            CliCommandManager commandManager = new CliCommandManager(socketChannel);

            String hostname;
            int port;

            if (args.length != 0 && args[0].equals("cli")) {
                controller = new CliController();
            } else if (args.length != 0 && args[0].equals("win")) {
                controller = new WinController();
                window = new Window(commandManager, controller);
                isWin = true;
            } else {
                throw new IllegalArgumentException("Usage: java -cp target/class client.ClientMain <cli/win>");
            }

            List<String> settings = controller.getSettings();

            if (isWin) {
                hostname = window.getIp();
                port = window.getPort();
            } else {
                hostname = settings.get(0);
                port = Integer.parseInt(settings.get(1));
            }

            socketChannel.connect(new InetSocketAddress(hostname, port));
            listener = new Listener(socketChannel, window);

            listener.start();

            String[] parts;
            String line;

            if (window == null) {
                System.out.println("Enter command: list, logout, message-<message>, login-<username>-<password>, exit");
            }

            do {
                line = controller.getLine();
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
                            case "message":
                                if (parts.length != 2) {
                                    throw new IllegalArgumentException("Usage: message-<message>");
                                }
                                commandManager.clientMes(parts[1]);
                                break;
                            case "login":
                                if (parts.length != 3) {
                                    throw new IllegalArgumentException("Usage: login-<username>-<password>");
                                }
                                commandManager.login(parts[1], parts[2]);
                                break;
                            case "exit":
                                commandManager.stop();
                                break;
                            default:
                                break;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            } while (commandManager.isFlag());

        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getLocalizedMessage());
        } finally {

            if (listener != null) {
                listener.stopListener();
            }

            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }

            if (window != null) {
                window.dispose();
            }

        }
    }
}