package xml.commands;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlType(propOrder = {"name"})
public class Logout {
    private String name;

    public Logout() {
        this.name = "logout";
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
