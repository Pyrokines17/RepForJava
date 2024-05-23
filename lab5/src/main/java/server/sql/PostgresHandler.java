package server.sql;

import java.io.*;
import java.sql.*;

public class PostgresHandler {
    private String ip;
    private String port;
    private String name;
    private String username;
    private String password;

    public Connection setConnection() throws ClassNotFoundException, SQLException, IOException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        String url = "jdbc:postgresql://"+ip+":"+port+"/"+name;
        return DriverManager.getConnection(url, username, password);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
