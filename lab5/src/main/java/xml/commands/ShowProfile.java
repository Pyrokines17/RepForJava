package xml.commands;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlType(propOrder = {"name", "username"})
public class ShowProfile {
    private String name;
    private String username;

    public ShowProfile() {
        this.name = "showprofile";
    }

    public ShowProfile(String username) {
        this.name = "showprofile";
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
