package server;

import java.io.*;
import java.nio.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import xml.commands.*;
import jakarta.xml.bind.*;

public class SerCommandManager {
    private final Connection connectionWithPostgres;

    private final static String GET_COUNT_SQL = "SELECT COUNT(*) FROM accounts;";
    private final static String INSERT_USERS_SQL = "INSERT INTO accounts" +
            "  (user_id, username, password, email, created_at, last_login) VALUES " +
            " (?, ?, ?, ?, ?, ?);";

    public SerCommandManager(Connection connectionWithPostgres) {
        this.connectionWithPostgres = connectionWithPostgres;
    }

    public void parse(ByteBuffer bufForMes) {
        String xmlString = new String(bufForMes.array(), Charset.defaultCharset());
        String[] parts = xmlString.split("\n");
        String name = parts[1].split("\"")[1];

        switch (name) {
            case "login":
                login(bufForMes);
                break;
            case "list":
                list();
                break;
            case "message":
                message(bufForMes);
                break;
            case "logout":
                logout();
                break;
        }
    }

    private void login(ByteBuffer bufForMes) {
        try {
            JAXBContext context = JAXBContext.newInstance(Login.class);
            Login login = (Login) context.createUnmarshaller().unmarshal(new ByteArrayInputStream(bufForMes.array()));
            PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(INSERT_USERS_SQL);

            preparedStatement.setInt(1, getCount() + 1);
            preparedStatement.setString(2, login.getUsername());
            preparedStatement.setString(3, generateHash(login.getPassword()));
            preparedStatement.setString(4, generateEmail());
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException | JAXBException e) {
            e.getLocalizedMessage();
        }
    }

    private void list() {

    }

    private void message(ByteBuffer bufForMes) {

    }

    private void logout() {

    }

    private int getCount() throws SQLException {
        PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(GET_COUNT_SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private String generateEmail() {
        String uniqueID = String.format("%s_%s", UUID.randomUUID().toString().substring(0, 5), System.currentTimeMillis() % 1000);
        return String.format("%s@%s", uniqueID, "gmail.com");
    }

    private String generateHash(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
