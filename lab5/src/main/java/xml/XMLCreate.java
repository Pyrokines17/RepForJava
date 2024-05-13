package xml;

import java.io.*;
import jakarta.xml.bind.*;
import xml.events.Error;
import xml.events.*;
import xml.commands.*;

public class XMLCreate {
    StringWriter writer = new StringWriter();

    public String getLogin(String username, String password) {
        Login login = new Login(username, password);

        try {
            JAXBContext context = JAXBContext.newInstance(Login.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(login, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getList() {
        List list = new List();

        try {
            JAXBContext context = JAXBContext.newInstance(List.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(list, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getClientMes(String message) {
        ClientMes clientMessage = new ClientMes(message);

        try {
            JAXBContext context = JAXBContext.newInstance(ClientMes.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(clientMessage, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getLogout() {
        Logout logout = new Logout();

        try {
            JAXBContext context = JAXBContext.newInstance(Logout.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(logout, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getUserlogout(String username) {
        Userlogout userlogout = new Userlogout(username);

        try {
            JAXBContext context = JAXBContext.newInstance(Userlogout.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userlogout, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getUserlogin(String username) {
        Userlogin userlogin = new Userlogin(username);

        try {
            JAXBContext context = JAXBContext.newInstance(Userlogin.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userlogin, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getServerMes(String username, String message) {
        ServerMes serverMessage = new ServerMes(username, message);

        try {
            JAXBContext context = JAXBContext.newInstance(ServerMes.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(serverMessage, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getSuccess() {
        Success success = new Success();

        try {
            JAXBContext context = JAXBContext.newInstance(Success.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(success, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getError() {
        Error error = new Error();

        try {
            JAXBContext context = JAXBContext.newInstance(Error.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(error, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }
}
