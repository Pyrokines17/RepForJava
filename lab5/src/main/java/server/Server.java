package server;

import java.sql.*;

public class Server {
    public static void main (String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost:32770/postgres";
        Class.forName("org.postgresql.Driver");

        Connection connection = DriverManager.getConnection(url, "postgres", null);
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM accounts;");

        resultSet.next();
        System.out.println(resultSet.getString("username"));

        connection.close();
    }
}
