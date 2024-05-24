package client;

import view.Window;
import xml.events.*;
import xml.events.Error;
import jakarta.xml.bind.*;
import xml.events.files.*;
import java.nio.charset.*;
import xml.events.list.*;
import java.nio.file.*;
import java.util.*;
import java.nio.*;
import java.io.*;

public class EventParser {
    public static String parse(ByteBuffer event, String path, Window window) {
        StringBuilder result = new StringBuilder();
        JAXBContext context;

        String xmlString = new String(event.array(), Charset.defaultCharset());
        Scanner scanner = new Scanner(xmlString);
        scanner.nextLine();
        String firstLine = scanner.nextLine();

        try {
            switch (firstLine) {
                case "<error>" -> {
                    context = JAXBContext.newInstance(Error.class);
                    Error error = (Error) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                    result = new StringBuilder("Error: " + error.getMessage());
                }
                case "<success></success>", "<success/>" -> result = new StringBuilder("Success");
                case "<success>" -> {
                    String secondLine = scanner.nextLine();
                    String thirdLine = scanner.nextLine();
                    if (secondLine.contains("users")) {
                        context = JAXBContext.newInstance(ListSuccess.class);
                        ListSuccess success =
                                (ListSuccess) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));

                        result = new StringBuilder("Users: ");

                        if (window != null) {
                            window.clearUsers();
                            window.updateUsers("Users:", true);
                        }

                        for (ListUser user : success.getUsers().getUsers()) {
                            result.append(user.getUsername()).append("|");

                            if (window != null) {
                                window.updateUsers(user.getUsername(), true);
                            }
                        }
                    } else if (secondLine.contains("id") && thirdLine.contains("/success")) {
                        context = JAXBContext.newInstance(FileSuccess.class);
                        FileSuccess success = (FileSuccess) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));

                        result = new StringBuilder("Success: " + success.getMessage());
                    } else if (secondLine.contains("id") && thirdLine.contains("name")) {
                        context = JAXBContext.newInstance(xml.events.files.Download.class);
                        Download download = (Download) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));

                        Path filePath = Path.of(path, download.getName());
                        Files.createFile(filePath);

                        byte[] decodedContent = Base64.getDecoder().decode(download.getContent());
                        Files.write(filePath, decodedContent);

                        return "Success: file download to "+filePath;
                    } else if (secondLine.contains("files")) {
                        context = JAXBContext.newInstance(LFSuccess.class);
                        LFSuccess lfSuccess = (LFSuccess) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));

                        List<ListFile> listOfFiles = lfSuccess.getFiles().getFiles();
                        result = new StringBuilder("You can download: \n");
                        StringBuilder subResult = new StringBuilder();

                        if (window != null) {
                            window.initFiles();
                        }

                        if (listOfFiles != null) {
                            for (ListFile bufFile : listOfFiles) {
                                subResult.append("New file: ").append(bufFile.getFilename()).append("; from: ").append(bufFile.getFrom()).append("; size: ").append(bufFile.getSize()).append("; mime: ").append(bufFile.getMimeType()).append("; id: ").append(bufFile.getId());

                                if (window != null) {
                                    window.updateFiles(subResult.toString());
                                }

                                result.append(subResult).append("\n");
                                subResult.setLength(0);
                            }
                        }

                    } else {
                        result = new StringBuilder("Unknown event");
                    }
                }
                case "<event name=\"message\">" -> {
                    context = JAXBContext.newInstance(ServerMes.class);
                    ServerMes message = (ServerMes) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                    result = new StringBuilder("Message from " + message.getFrom() + ": " + message.getMessage());
                }
                case "<event name=\"userlogin\">" -> {
                    context = JAXBContext.newInstance(Userlogin.class);
                    Userlogin user = (Userlogin)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                    result = new StringBuilder("User " + user.getUsername() + " logged in");

                    if (window != null) {
                        window.updateUsers(user.getUsername(), true);
                    }
                }
                case "<event name=\"userlogout\">" -> {
                    context = JAXBContext.newInstance(Userlogout.class);
                    Userlogout userLogout = (Userlogout) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                    result = new StringBuilder("User " + userLogout.getUsername() + " logged out");

                    if (window != null) {
                        window.updateUsers(userLogout.getUsername(), false);
                    }
                }
                case "<event name=\"file\">" -> {
                    context = JAXBContext.newInstance(NewFile.class);
                    NewFile newFile = (NewFile) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                    result = new StringBuilder("New file: "+newFile.getFileName()+"; from: "+newFile.getFrom()+"; size: "+newFile.getSize()+"; mime: "+newFile.getMimeType()+"; id: "+newFile.getId());

                    if (window != null) {
                        window.updateFiles(result.toString());
                    }
                }
                default -> result = new StringBuilder("Unknown event");
            }
        } catch (JAXBException | IOException e) {
            System.err.println(e.getLocalizedMessage());
        }

        return result.toString();
    }
}
