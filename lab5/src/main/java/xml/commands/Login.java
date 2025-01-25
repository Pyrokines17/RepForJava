package xml.commands;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlType(propOrder = {"name", "username", "password"})
public class Login {
    private String name;
    private String username;
    private String password;

    public Login() {
        this.name = "login";
    }

    public Login(String username, String password) {
        this.name = "login";
        this.username = username;
        this.password = password;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
