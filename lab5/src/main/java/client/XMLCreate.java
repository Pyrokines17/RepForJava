package client;

import xml.*;
import java.io.*;
import javax.xml.bind.*;

public class XMLCreate {

    public String getLogin(String username, String password) throws JAXBException {
        Login login = new Login();
        login.setName("login");
        login.setUsername(username);
        login.setPassword(password);

        JAXBContext context = JAXBContext.newInstance(Login.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(login, new File("./login.xml"));

        return "finish";
    }

}
