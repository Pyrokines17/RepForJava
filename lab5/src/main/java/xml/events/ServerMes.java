package xml.events;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "event")
@XmlType(propOrder = {"name", "from", "message"})
public class ServerMes {
    private String name;
    private String from;
    private String message;

    public ServerMes() {
        this.name = "message";
    }

    public ServerMes(String from, String message) {
        this.name = "message";
        this.from = from;
        this.message = message;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "from")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
