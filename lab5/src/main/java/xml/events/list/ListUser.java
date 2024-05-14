package xml.events.list;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class ListUser {
    private String username;

    public ListUser() {}

    public ListUser(String username) {
        this.username = username;
    }

    @XmlElement(name = "name")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
