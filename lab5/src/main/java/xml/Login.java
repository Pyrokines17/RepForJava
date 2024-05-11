package xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlType(propOrder = {"name", "username", "password"})
public class Login {
    private String name;
    private String username;
    private String password;

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
