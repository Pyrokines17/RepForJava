package server.sql;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import xml.commands.*;
import org.springframework.security.crypto.bcrypt.*;

public class SQLWorker {
    private final static String GET_COUNT_SQL = "SELECT COUNT(*) FROM accounts;";
    private final static String GET_PASSWORD_SQL = "SELECT password FROM accounts WHERE username = ?;";
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean checkUser(PreparedStatement checkUser, Login login) throws SQLException {
        checkUser.setString(1, login.getUsername());
        ResultSet resultSet = checkUser.executeQuery();
        resultSet.next();

        int count = resultSet.getInt(1);
        return count > 0;
    }

    public String getPassword(Connection connectionWithSQL, Login login) throws SQLException {
        PreparedStatement getPassword = connectionWithSQL.prepareStatement(GET_PASSWORD_SQL);
        getPassword.setString(1, login.getUsername());
        ResultSet resultSet = getPassword.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    public void addUser(PreparedStatement preparedStatement, Login login, Connection connection)
            throws SQLException {

        preparedStatement.setInt(1, getCount(connection));
        preparedStatement.setString(2, login.getUsername());
        preparedStatement.setString(3, passwordEncoder.encode(login.getPassword()));
        preparedStatement.setString(4, generateEmail());
        preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
        Logger.getGlobal().info(preparedStatement.toString());
        preparedStatement.executeUpdate();
    }

    private int getCount(Connection connectionWithPostgres) throws SQLException {
        PreparedStatement preparedStatement = connectionWithPostgres.prepareStatement(GET_COUNT_SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private String generateEmail() {
        String uniqueID = String.format("%s_%s", UUID.randomUUID().toString().substring(0, 5), System.currentTimeMillis() % 1000);
        return String.format("%s@%s", uniqueID, "gmail.com");
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
