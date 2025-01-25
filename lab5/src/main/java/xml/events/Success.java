package xml.events;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "success")
@XmlType(propOrder = {"message"})
public class Success {
    private String message;

    public Success() {
        this.message = null;
    }

    @XmlElement
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
