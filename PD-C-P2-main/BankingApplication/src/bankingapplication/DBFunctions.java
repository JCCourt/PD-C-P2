package bankingapplication;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBFunctions {

    private static final String URL = "jdbc:derby:BankTellerDB;create=true"; // Embedded mode connection URL
    private Connection connection;

    public DBFunctions() {
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAccount(String firstName, String lastName, double balance) {
        String query = "INSERT INTO APP.ACCOUNTS (FIRSTNAME, LASTNAME, BALANCE) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setDouble(3, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addForeignAccount(String firstName, String lastName, double initialBalance, String currency) {
        String query = "INSERT INTO APP.FOREIGNACCOUNTS (FIRSTNAME, LASTNAME, AUD, USD, GBP, EUR, JPY) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setDouble(3, currency.equals("AUD") ? initialBalance : 0.0);
            statement.setDouble(4, currency.equals("USD") ? initialBalance : 0.0);
            statement.setDouble(5, currency.equals("GBP") ? initialBalance : 0.0);
            statement.setDouble(6, currency.equals("EUR") ? initialBalance : 0.0);
            statement.setDouble(7, currency.equals("JPY") ? initialBalance : 0.0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(String firstName, String lastName, boolean isForeign) {
        String query = isForeign 
            ? "DELETE FROM APP.FOREIGNACCOUNTS WHERE FIRSTNAME = ? AND LASTNAME = ?"
            : "DELETE FROM APP.ACCOUNTS WHERE FIRSTNAME = ? AND LASTNAME = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(String firstName, String lastName, double amount) {
        String query = "UPDATE APP.ACCOUNTS SET BALANCE = BALANCE + ? WHERE FIRSTNAME = ? AND LASTNAME = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(String firstName, String lastName, double amount, String currency) {
        String query = "UPDATE APP.FOREIGNACCOUNTS SET " + currency + " = " + currency + " + ? WHERE FIRSTNAME = ? AND LASTNAME = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean withdraw(String firstName, String lastName, double amount) {
        Account account = getAccount(firstName, lastName);
        if (account != null && account.getBalance() >= amount) {
            String query = "UPDATE APP.ACCOUNTS SET BALANCE = BALANCE - ? WHERE FIRSTNAME = ? AND LASTNAME = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDouble(1, amount);
                statement.setString(2, firstName);
                statement.setString(3, lastName);
                statement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean withdrawForeignAccount(String firstName, String lastName, double amount, String currency) {
        ForeignAccount account = getForeignAccount(firstName, lastName);
        if (account != null && account.getCurrencyBalances().getOrDefault(currency, 0.0) >= amount) {
            String query = "UPDATE APP.FOREIGNACCOUNTS SET " + currency + " = " + currency + " - ? WHERE FIRSTNAME = ? AND LASTNAME = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDouble(1, amount);
                statement.setString(2, firstName);
                statement.setString(3, lastName);
                statement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Account getAccount(String firstName, String lastName) {
        String query = "SELECT * FROM APP.ACCOUNTS WHERE FIRSTNAME = ? AND LASTNAME = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double balance = resultSet.getDouble("BALANCE");
                    return new Account(firstName + " " + lastName, balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ForeignAccount getForeignAccount(String firstName, String lastName) {
        String query = "SELECT * FROM APP.FOREIGNACCOUNTS WHERE FIRSTNAME = ? AND LASTNAME = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ForeignAccount account = new ForeignAccount(firstName + " " + lastName);
                    account.deposit(resultSet.getDouble("AUD"), "AUD");
                    account.deposit(resultSet.getDouble("USD"), "USD");
                    account.deposit(resultSet.getDouble("GBP"), "GBP");
                    account.deposit(resultSet.getDouble("EUR"), "EUR");
                    account.deposit(resultSet.getDouble("JPY"), "JPY");
                    return account;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void transfer(String fromFirstName, String fromLastName, String toFirstName, String toLastName, double amount) {
        try {
            connection.setAutoCommit(false);
            if (withdraw(fromFirstName, fromLastName, amount)) {
                deposit(toFirstName, toLastName, amount);
                connection.commit();
            } else {
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Insufficient funds for transfer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void transferForeign(String fromFirstName, String fromLastName, String toFirstName, String toLastName, double amount, String currency) {
        try {
            connection.setAutoCommit(false);
            if (withdrawForeignAccount(fromFirstName, fromLastName, amount, currency)) {
                deposit(toFirstName, toLastName, amount, currency);
                connection.commit();
            } else {
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Insufficient funds for transfer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public double getBalance(String firstName, String lastName) {
        Account account = getAccount(firstName, lastName);
        if (account != null) {
            return account.getBalance();
        }
        return 0.0;
    }

    public Map<String, Double> getForeignBalance(String firstName, String lastName) {
        ForeignAccount account = getForeignAccount(firstName, lastName);
        if (account != null) {
            return account.getCurrencyBalances();
        }
        return new HashMap<>();
    }
}
