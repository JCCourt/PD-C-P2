package bankingapplication;

public class DBFunctions {
    
}

/*

package Task08_2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookStore {

    private final DBManager dbManager;
    private final Connection conn;
    private Statement statement;

    public BookStore() {
        dbManager = new DBManager();
        conn = dbManager.getConnection();
    }

    public void connectBookStoreDB() {
        try {
            dbManager.establishConnection();
            System.out.println("Database connected successfully.");
        } catch (Exception e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    public void createPromotionTable() {
        try {
            dropTableIfExists("PROMOTION");
            statement = conn.createStatement();
            String createTableSQL = "CREATE TABLE PROMOTION (CATEGORY VARCHAR(20), DISCOUNT INT)";
            statement.executeUpdate(createTableSQL);
            System.out.println("PROMOTION table created successfully.");

            String insertDataSQL = "INSERT INTO PROMOTION VALUES ('Fiction', 0), ('Non-fiction', 10), ('Textbook', 30)";
            statement.executeUpdate(insertDataSQL);
            System.out.println("Data inserted into PROMOTION table successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating or populating the PROMOTION table: " + e.getMessage());
        }
    }

    public ResultSet getWeekSpecial() throws SQLException {
        ResultSet rs = null;
        try {
            String query = "SELECT BOOK.TITLE, BOOK.PRICE, PROMOTION.DISCOUNT, (BOOK.PRICE * (1 - PROMOTION.DISCOUNT / 100.0)) AS SPECIAL_PRICE FROM BOOK JOIN PROMOTION ON BOOK.CATEGORY = PROMOTION.CATEGORY";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            System.out.println("Week specials retrieved successfully.");
        } catch (SQLException e) {
            System.err.println("Error fetching week specials: " + e.getMessage());
        }
        return rs;
    }

    public void createWeekSpecialTable(ResultSet rs) {
        try {
            dropTableIfExists("WEEK_SPECIAL");
            statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE WEEK_SPECIAL (TITLE VARCHAR(100), SPECIAL_PRICE DECIMAL(10, 2))");

            while (rs.next()) {
                String title = rs.getString("TITLE");
                double specialPrice = rs.getDouble("SPECIAL_PRICE");
                statement.executeUpdate(String.format("INSERT INTO WEEK_SPECIAL VALUES ('%s', %.2f)", title, specialPrice));
            }
            System.out.println("WEEK_SPECIAL table created and populated successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating WEEK_SPECIAL table: " + e.getMessage());
        }
    }

    public void dropTableIfExists(String tableName) {
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet rs = dbm.getTables(null, null, tableName.toUpperCase(), null);
            statement = conn.createStatement();
            if (rs.next()) {
                statement.executeUpdate("DROP TABLE " + tableName);
                System.out.println(tableName + " table dropped successfully.");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error dropping " + tableName + " table: " + e.getMessage());
        }
    }
}

*/