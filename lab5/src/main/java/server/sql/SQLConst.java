package server.sql;

public class SQLConst {
    private final static String GET_COUNT_SQL = "SELECT COUNT(*) FROM accounts;";
    private final static String GET_PASSWORD_SQL = "SELECT password FROM accounts WHERE username = ?;";
    private final static String UPDATE_LAST_LOGIN = "UPDATE accounts SET last_login = ? WHERE username = ?;";
    private final static String CHECK_USER_SQL = "SELECT COUNT(*) FROM accounts WHERE username = ?;";
    private final static String INSERT_USERS_SQL = "INSERT INTO accounts" +
            "  (user_id, username, password, email, created_at, last_login) VALUES " +
            " (?, ?, ?, ?, ?, ?);";

    public static String getGetCountSql() {
        return GET_COUNT_SQL;
    }

    public static String getGetPasswordSql() {
        return GET_PASSWORD_SQL;
    }

    public static String getUpdateLastLogin() {
        return UPDATE_LAST_LOGIN;
    }

    public static String getCheckUserSql() {
        return CHECK_USER_SQL;
    }

    public static String getInsertUsersSql() {
        return INSERT_USERS_SQL;
    }
}
