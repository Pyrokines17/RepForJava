package xml.events;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "error")
@XmlType(propOrder = {"message"})
public class Error {
    private String message;

    public Error() {
        this.message = null;
    }

    public Error(String message) {
        this.message = message;
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
