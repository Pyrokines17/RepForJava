package xml.events.list;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "success")
public class ListSuccess {
    private ListUsers users;

    public ListSuccess() {}

    public ListSuccess(ListUsers users) {
        this.users = users;
    }

    @XmlElement(name = "users")
    public ListUsers getUsers() {
        return users;
    }

    public void setUsers(ListUsers users) {
        this.users = users;
    }
}
