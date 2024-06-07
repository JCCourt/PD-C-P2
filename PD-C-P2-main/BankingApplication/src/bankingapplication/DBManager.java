package bankingapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static Connection conn;
    private static final String URL = "jdbc:derby:BankTellerDB;create=true";

    static {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Derby driver loaded successfully.");
            establishConnection();
        } catch (ClassNotFoundException e) {
            System.err.println("Derby driver not found.");
        } catch (SQLException e) {
            System.err.println("Database connection could not be established on static initialization.");
            printSQLException(e);
        }
    }

    public static synchronized Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("Re-establishing database connection...");
                establishConnection();
            }
        } catch (SQLException e) {
            System.err.println("Failed to check or re-establish connection: " + e.getMessage());
            printSQLException(e);
        }
        return conn;
    }

    private static void establishConnection() throws SQLException {
        System.out.println("Attempting to establish database connection...");
        System.out.println("Connection URL: " + URL);
        conn = DriverManager.getConnection(URL);
        System.out.println("Database connection established at " + conn.getMetaData().getURL());
    }

    public static void shutdownDatabase() {
        try {
            if (conn != null) {
                System.out.println("Closing database connection...");
                conn.close();
            }
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            if (!ex.getSQLState().equals("XJ015")) {
                System.err.println("Database did not shut down normally: " + ex.getMessage());
            } else {
                System.out.println("Database shut down normally.");
            }
        }
    }

    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                System.err.println("SQLException encountered: " + e.getMessage());
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.err.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
