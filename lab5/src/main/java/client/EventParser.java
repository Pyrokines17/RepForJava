package client;

import xml.events.*;
import xml.events.Error;
import jakarta.xml.bind.*;
import xml.events.list.*;
import java.nio.charset.*;
import java.nio.*;
import java.io.*;

public class EventParser {
    public static String parse(byte[] event) {
        StringBuilder result = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.wrap(event);
        JAXBContext context;

        String xmlString = new String(buffer.array(), Charset.defaultCharset());

        String[] parts = xmlString.split("\n");

        try {
            switch (parts[1]) {
                case "<error>" -> {
                    context = JAXBContext.newInstance(Error.class);
                    Error error = (Error) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event));
                    result = new StringBuilder("Error: " + error.getMessage());
                }
                case "<success></success>", "<success/>" -> result = new StringBuilder("Success");
                case "<success>" -> {
                    context = JAXBContext.newInstance(ListSuccess.class);
                    ListSuccess success =
                            (ListSuccess) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event));

                    result = new StringBuilder("Users: ");
                    for (ListUser user : success.getUsers().getUsers()) {
                        result.append(user.getUsername()).append("|");
                    }
                }
                case "<event name=\"message\">" -> {
                    context = JAXBContext.newInstance(ServerMes.class);
                    ServerMes message = (ServerMes) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event));
                    result = new StringBuilder("Message from " + message.getFrom() + ": " + message.getMessage());
                }
                case "<event name=\"userlogin\">" -> {
                    context = JAXBContext.newInstance(Userlogin.class);
                    Userlogin user = (Userlogin)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event));
                    result = new StringBuilder("User " + user.getUsername() + " logged in");
                }
                case "<event name=\"userlogout\">" -> {
                    context = JAXBContext.newInstance(Userlogout.class);
                    Userlogout userLogout = (Userlogout) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(event));
                    result = new StringBuilder("User " + userLogout.getUsername() + " logged out");
                }
                default -> result = new StringBuilder("Unknown event");
            }
        } catch (JAXBException e) {
            System.err.println(e.getLocalizedMessage());
        }

        return result.toString();
    }
}
