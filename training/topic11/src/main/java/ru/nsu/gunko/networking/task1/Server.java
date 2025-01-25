package ru.nsu.gunko.networking.task1;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        String basePath = "/home/pyro/test";

        try (ServerSocket serverSocket = new ServerSocket(8008)) {

            while(true) {

                try (Socket socket = serverSocket.accept();
                     BufferedReader request = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    String lineOfRequest = request.readLine();
                    String[] parts = lineOfRequest.split(" ");
                    String path = parts[1];

                    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                        File f = new File(basePath + path);

                        if (f.exists()) {
                            if (!f.isDirectory()) {
                                String line;

                                try (BufferedReader reader = new BufferedReader(new FileReader(basePath + path))) {

                                    while ((line = reader.readLine()) != null) {
                                        writer.write(line + "\n");
                                        writer.flush();
                                    }
                                }

                            } else {
                                writer.write("Is directory, replay request please");
                            }

                        } else {
                            writer.write("404: This not found\n");
                        }

                    }

                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
