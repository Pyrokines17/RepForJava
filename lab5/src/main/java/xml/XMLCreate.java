package xml;

import java.io.*;
import xml.events.*;
import xml.commands.*;
import xml.events.Error;
import xml.events.list.*;
import jakarta.xml.bind.*;

public class XMLCreate {
    StringWriter writer = new StringWriter();

    public String getLogin(String username, String password) {
        writer.getBuffer().setLength(0);
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
        writer.getBuffer().setLength(0);
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
        writer.getBuffer().setLength(0);
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
        writer.getBuffer().setLength(0);
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
        writer.getBuffer().setLength(0);
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
        writer.getBuffer().setLength(0);
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
        writer.getBuffer().setLength(0);
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
        writer.getBuffer().setLength(0);
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

    public String getError(String errorMes) {
        writer.getBuffer().setLength(0);
        Error error = new Error(errorMes);

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

    public String getListSuccess(ListUsers listUsers) {
        writer.getBuffer().setLength(0);
        ListSuccess listSuccess = new ListSuccess(listUsers);

        try {
            JAXBContext context = JAXBContext.newInstance(ListSuccess.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(listSuccess, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }
}
