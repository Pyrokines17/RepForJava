package xml.events.list;

import java.util.*;
import xml.commands.Login;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "users")
public class ListUsers {
    private List<ListUser> users;

    public ListUsers() {}

    public ListUsers(List<ListUser> users) {
        this.users = users;
    }

    @XmlElement(name = "user")
    public List<ListUser> getUsers() {
        return users;
    }

    public void setUsers(List<ListUser> users) {
        this.users = users;
    }

    public void addUser(Login login) {
        if (users == null) {
            users = new ArrayList<>();
        }

        users.add(new ListUser(login.getUsername()));
    }
}
