package client;

import java.io.*;
import java.nio.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.List;
import java.nio.file.*;
import java.util.logging.*;

import view.Window;
import view.UserProfile;

import xml.events.*;
import xml.events.Error;
import xml.events.list.*;
import xml.events.files.*;
import javax.xml.stream.*;
import jakarta.xml.bind.*;
import javax.xml.stream.events.*;

public class EventParser {
    private final static XMLInputFactory factory = XMLInputFactory.newInstance();

    public static String parse(ByteBuffer event, String path, Window window) {
        String avatarPath = "src/main/java/client/avatars/";
        StringBuilder result = new StringBuilder();
        StartElement startElement = null;
        XMLEventReader reader = null;
        String firstLine = null;
        JAXBContext context;

        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(event.array());
            reader = factory.createXMLEventReader(new InputStreamReader(stream));
            XMLEvent xmlEvent = reader.nextEvent();

            while (!xmlEvent.isStartElement()) {
                xmlEvent = reader.nextEvent();
            }

            startElement = xmlEvent.asStartElement();
            firstLine = startElement.getName().getLocalPart();
        } catch (XMLStreamException e) {
            Logger.getGlobal().info(e.getLocalizedMessage());
        }

        assert startElement != null;

        try {
            switch (firstLine) {
                case "error" -> {
                    context = JAXBContext.newInstance(Error.class);
                    Error error = (Error) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                    result = new StringBuilder("Error: " + error.getMessage());
                }
                case "event" -> {
                    Iterator<Attribute> iterator = startElement.getAttributes();
                    Attribute attribute = iterator.next();
                    String secondLine = attribute.getValue();

                    switch (secondLine) {
                        case "message" -> {
                            context = JAXBContext.newInstance(ServerMes.class);
                            ServerMes message = (ServerMes) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                            result = new StringBuilder("Message from " + message.getFrom() + ": " + message.getMessage());
                        }
                        case "userlogin" -> {
                            context = JAXBContext.newInstance(Userlogin.class);
                            Userlogin user = (Userlogin)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                            result = new StringBuilder("User " + user.getUsername() + " logged in");

                            if (window != null) {
                                window.updateUsers(user.getUsername(), true);
                            }
                        }
                        case "userlogout" -> {
                            context = JAXBContext.newInstance(Userlogout.class);
                            Userlogout userLogout = (Userlogout) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                            result = new StringBuilder("User " + userLogout.getUsername() + " logged out");

                            if (window != null) {
                                window.updateUsers(userLogout.getUsername(), false);
                            }
                        }
                        case "file" -> {
                            context = JAXBContext.newInstance(NewFile.class);
                            NewFile newFile = (NewFile) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                            result = new StringBuilder("New file: "+newFile.getFileName()+"; from: "+newFile.getFrom()+"; size: "+newFile.getSize()+"; mime: "+newFile.getMimeType()+"; id: "+newFile.getId());

                            if (window != null) {
                                window.updateFiles(result.toString());
                            }
                        }
                        default -> result = new StringBuilder("Unknown event");
                    }
                }
                case "success" -> {
                    String secondLine = null;

                    try {
                        XMLEvent xmlEvent = reader.nextEvent();

                        while (!xmlEvent.isEndElement() && !xmlEvent.isStartElement()) {
                            xmlEvent = reader.nextEvent();
                        }

                        if (xmlEvent.isEndElement()) {
                            result = new StringBuilder("success");
                            break;
                        }

                        StartElement element = xmlEvent.asStartElement();
                        secondLine = element.getName().getLocalPart();
                    } catch (XMLStreamException e) {
                        Logger.getGlobal().info(e.getLocalizedMessage());
                    }

                    assert secondLine != null;

                    switch (secondLine) {
                        case "users" -> {
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
                        }
                        case "files" -> {
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
                            } else {
                                result.append("Nothing to download");
                            }
                        }
                        case "username" -> {
                            context = JAXBContext.newInstance(Profile.class);
                            Profile profile = (Profile) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));

                            Path filePath = Path.of(avatarPath, profile.getFilename());

                            if (Files.exists(filePath)) {
                                Files.delete(filePath);
                            }

                            Files.createFile(filePath);
                            byte[] decodedContent = Base64.getDecoder().decode(profile.getContent());
                            Files.write(filePath, decodedContent);

                            ImageIcon imageIcon = new ImageIcon(
                                    new ImageIcon(filePath.toString()).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));

                            UserProfile userProfile = new UserProfile(window);
                            userProfile.setUserProfile(profile.getUsername(), profile.getStatus(), imageIcon);

                            Dimension size = window.getScreenSize();
                            userProfile.setSize(size.width / 8, size.height / 4);

                            userProfile.setVisible(true);
                            result = new StringBuilder("Success");
                        }
                        case "id" -> {
                            try {
                                XMLEvent xmlEvent = reader.nextEvent();

                                while (!xmlEvent.isEndElement() && !xmlEvent.isStartElement()) {
                                    xmlEvent = reader.nextEvent();
                                }

                                if (xmlEvent.isEndElement()) {
                                    context = JAXBContext.newInstance(FileSuccess.class);
                                    FileSuccess success = (FileSuccess) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));
                                    result = new StringBuilder("Success: " + success.getMessage());
                                    break;
                                }

                                context = JAXBContext.newInstance(Download.class);
                                Download download = (Download) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event.array()));

                                Path filePath = Path.of(path, download.getName());
                                Files.createFile(filePath);

                                byte[] decodedContent = Base64.getDecoder().decode(download.getContent());
                                Files.write(filePath, decodedContent);

                                result = new StringBuilder("Success: file downloaded");
                            } catch (XMLStreamException e) {
                                Logger.getGlobal().info(e.getLocalizedMessage());
                            }
                        }
                        default -> result = new StringBuilder("Unknown event");
                    }
                }
                default -> result = new StringBuilder("Unknown event");
            }
        } catch (JAXBException | IOException e) {
            Logger.getGlobal().info(e.getLocalizedMessage());
        }

        return result.toString();
    }
}
