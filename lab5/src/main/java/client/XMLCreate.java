package client;

import xml.*;
import java.io.*;
import jakarta.xml.bind.*;

public class XMLCreate {

    public String getLogin(String username, String password) throws JAXBException {
        Login login = new Login();
        login.setName("login");
        login.setUsername(username);
        login.setPassword(password);

        StringWriter writer = new StringWriter();

        JAXBContext context = JAXBContext.newInstance(Login.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(login, writer);

        return writer.toString();
    }

}
