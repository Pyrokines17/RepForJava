package xml.events.files;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "success")
@XmlType(propOrder = {"message"})
public class FileSuccess {
    private String message;

    public FileSuccess() {}

    public FileSuccess(String uuid) {
        this.message = uuid;
    }

    @XmlElement(name = "id")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
