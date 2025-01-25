package xml.events;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "event")
@XmlType(propOrder = {"name", "username"})
public class Userlogout {
    private String name;
    private String username;

    public Userlogout() {
        this.name = "userlogout";
    }

    public Userlogout(String username) {
        this.name = "userlogout";
        this.username = username;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
