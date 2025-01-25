package xml.events;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "success")
@XmlType(propOrder = {"username", "filename", "encoding", "content", "status"})
public class Profile {
    private String username;
    private String filename;
    private String encoding;
    private String content;
    private String status;

    public Profile() {
    }

    public Profile(String username, String status, String filename, String encoding, String content) {
        this.username = username;
        this.status = status;
        this.filename = filename;
        this.encoding = encoding;
        this.content = content;
    }

    @XmlElement(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "filename")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @XmlElement(name = "encoding")
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @XmlElement(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @XmlElement(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
