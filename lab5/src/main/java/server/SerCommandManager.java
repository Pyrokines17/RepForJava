package server;

import xml.*;
import java.io.*;
import java.nio.*;
import java.sql.*;
import java.util.*;
import xml.commands.*;
import jakarta.xml.bind.*;
import java.nio.charset.*;
import java.nio.channels.*;
import org.springframework.security.crypto.bcrypt.*;


public class SerCommandManager {
    private final Connection connectionWithPostgres;
    private final XMLCreate xmlCreate;

    private final static String GET_COUNT_SQL = "SELECT COUNT(*) FROM accounts;";
    private final static String INSERT_USERS_SQL = "INSERT INTO accounts" +
            "  (user_id, username, password, email, created_at, last_login) VALUES " +
            " (?, ?, ?, ?, ?, ?);";

    public SerCommandManager(Connection connectionWithPostgres) {
        this.connectionWithPostgres = connectionWithPostgres;
        this.xmlCreate = new XMLCreate();
    }

    public boolean parse(ByteBuffer bufForMes) {
        String xmlString = new String(bufForMes.array(), Charset.defaultCharset());
        String[] parts = xmlString.split("\n");
        String name = parts[1].split("\"")[1];

        return switch (name) {
            case "login" -> login(bufForMes);
            case "list" -> list();
            case "message" -> message(bufForMes);
            case "logout" -> logout();
            default -> false;
        };
    }

    private boolean login(ByteBuffer bufForMes) {
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
            return true;
        } catch (SQLException | JAXBException e) {
            e.getLocalizedMessage();
            return false;
        }
    }

    private boolean list() {
        return true;
    }

    private boolean message(ByteBuffer bufForMes) {
        return true;
    }

    private boolean logout() {
        return true;
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

    public void sendSuccess(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        String xmlString = xmlCreate.getSuccess();
        writeAnswer(xmlString, socketChannel);
    }

    public void sendError(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        String xmlString = xmlCreate.getError();
        writeAnswer(xmlString, socketChannel);
    }

    private void writeAnswer(String xmlString, SocketChannel socketChannel)
            throws IOException {

        int len = 4 + xmlString.getBytes().length;
        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.clear();
        buffer.putInt(xmlString.length());
        buffer.put(xmlString.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
