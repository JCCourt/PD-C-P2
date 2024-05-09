package bankingapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static Connection conn;
    private static final String USER_NAME = "pdc";  // DB username
    private static final String PASSWORD = "pdc";  // DB password
    private static final String URL = "jdbc:derby://localhost:1527/BankTellerDB";  // URL of the DB host

    static {
        try {
            //loads driver (incase error)
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load Derby driver.");
            e.printStackTrace();
        }
    }

    public DBManager() {
        establishConnection();
    }

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        System.out.println(dbManager.getConnection());
    }

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                System.out.println("Database connection established.");
            } catch (SQLException ex) {
                System.err.println("Failed to connect to the database: " + ex.getMessage());
                return null;
            }
        }
        return conn;
    }

    public static void establishConnection() {
        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException ex) {
            System.err.println("Failed to connect to the database: " + ex.getMessage());
        }
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static ResultSet queryDB(String sql) {
        if (conn == null) {
            System.out.println("No connection established. Trying to reconnect...");
            establishConnection();
            if (conn == null) {
                System.out.println("Still no connection.");
                return null;
            }
        }

        try (Statement statement = conn.createStatement()) {
            return statement.executeQuery(sql);
        } catch (SQLException ex) {
            System.err.println("Query failed: " + ex.getMessage());
            return null;
        }
    }

    public void updateDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
