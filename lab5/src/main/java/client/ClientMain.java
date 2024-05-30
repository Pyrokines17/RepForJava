package client;

import controller.*;
import view.Window;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.channels.*;
import java.util.logging.Logger;

public class ClientMain {

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        Window window = null;
        Listener listener;

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

            if (socketChannel.connect(new InetSocketAddress(hostname, port))) {
                Logger.getGlobal().info("Connected");
            }

            listener = new Listener(socketChannel, window);

            listener.start();

            String[] parts;
            String line;

            String info = "Enter command: list, logout, message-<message>, login-<username>-<password>, send-<path>, download-<id>-<path>, filelist, help, exit";

            if (window == null) {
                System.out.println(info);
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
                                commandManager.fileList();
                                commandManager.list();
                                break;
                            case "exit":
                                commandManager.stop();
                                break;
                            case "send":
                                if (parts.length != 2) {
                                    throw new IllegalArgumentException("Usage: send-<path>");
                                }
                                commandManager.sendFile(parts[1]);
                                break;
                            case "download":
                                if (parts.length != 3) {
                                    throw new IllegalArgumentException("Usage: download-<id>-<path>");
                                }
                                listener.setPath(parts[2]);
                                commandManager.download(parts[1]);
                                break;
                            case "help":
                                System.out.println(info);
                                break;
                            case "filelist":
                                commandManager.fileList();
                                break;
                            default:
                                break;
                        }
                    }
                } catch (IllegalArgumentException | IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }

            } while (commandManager.isFlag());

            listener.stopListener();
            commandManager.logout();

        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getLocalizedMessage());
        } finally {

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