package xml.events.list;

import jakarta.xml.bind.annotation.*;
import xml.commands.Login;

import java.util.*;


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
