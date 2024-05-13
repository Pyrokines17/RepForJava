package xml.events;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "event")
@XmlType(propOrder = {"name", "username"})
public class Userlogin {
    private String name;
    private String username;

    public Userlogin() {
        this.name = "userlogin";
    }

    public Userlogin(String username) {
        this.name = "userlogin";
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
}
