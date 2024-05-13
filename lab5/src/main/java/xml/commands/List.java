package xml.commands;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlType(propOrder = {"name"})
public class List {
    private String name;

    public List() {
        this.name = "list";
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
