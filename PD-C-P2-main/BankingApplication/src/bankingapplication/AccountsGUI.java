package bankingapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class AccountsGUI extends JPanel {
    private DBFunctions dbFunctions;
    private boolean isForeignAccount; // Flag to track account type
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    public AccountsGUI() {
        dbFunctions = new DBFunctions();
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        JPanel accountTypePanel = createAccountTypePanel();
        JPanel operationPanel = createOperationPanel();

        cardsPanel.add(accountTypePanel, "Account Types");
        cardsPanel.add(operationPanel, "Operations");

        setLayout(new BorderLayout());
        add(cardsPanel, BorderLayout.CENTER);

        showAccountTypePanel(); // Show initiall account type selection 
    }

    public void resetView() {
        showAccountTypePanel();
    }

    private void showAccountTypePanel() {
        cardLayout.show(cardsPanel, "Account Types");
    }

    private void showOperationsPanel() {
        cardLayout.show(cardsPanel, "Operations");
    }

    private JPanel createAccountTypePanel() {
        JButton regularAccountButton = new JButton("Regular Account");
        JButton foreignAccountButton = new JButton("Foreign Account");
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(regularAccountButton);
        panel.add(foreignAccountButton);

        regularAccountButton.addActionListener(e -> {
            isForeignAccount = false;
            showOperationsPanel();
        });
        foreignAccountButton.addActionListener(e -> {
            isForeignAccount = true;
            showOperationsPanel();
        });

        return panel;
    }

    private JPanel createOperationPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));

        JButton createAccountButton = new JButton("Create Account");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton getBalanceButton = new JButton("Get Balance");
        JButton deleteAccountButton = new JButton("Delete Account");

        createAccountButton.addActionListener(e -> createAccount());
        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        transferButton.addActionListener(e -> transfer());
        getBalanceButton.addActionListener(e -> getBalance());
        deleteAccountButton.addActionListener(e -> deleteAccount());

        panel.add(createAccountButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transferButton);
        panel.add(getBalanceButton);
        panel.add(deleteAccountButton);

        return panel;
    }

    private void createAccount() {
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        String initialBalanceStr = JOptionPane.showInputDialog("Enter Initial Balance:");
        double initialBalance = Double.parseDouble(initialBalanceStr);

        if (isForeignAccount) {
            // Create foreign account
            String currency = JOptionPane.showInputDialog("Enter Currency (AUD, USD, GBP, EUR, JPY):");
            dbFunctions.addForeignAccount(firstName, lastName, initialBalance, currency);
        } else {
            // Create regular account
            dbFunctions.addAccount(firstName, lastName, initialBalance);
        }
    }

    private void deposit() {
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        String amountStr = JOptionPane.showInputDialog("Enter Amount to Deposit:");
        double amount = Double.parseDouble(amountStr);

        if (isForeignAccount) {
            // Deposit foreign account
            String currency = JOptionPane.showInputDialog("Enter Currency (AUD, USD, GBP, EUR, JPY):");
            dbFunctions.deposit(firstName, lastName, amount, currency);
        } else {
            // Deposit regular account
            dbFunctions.deposit(firstName, lastName, amount);
        }
    }

    private void withdraw() {
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        String amountStr = JOptionPane.showInputDialog("Enter Amount to Withdraw:");
        double amount = Double.parseDouble(amountStr);

        boolean success;
        if (isForeignAccount) {
            // Withdraw from foreign account
            String currency = JOptionPane.showInputDialog("Enter Currency (AUD, USD, GBP, EUR, JPY):");
            success = dbFunctions.withdrawForeignAccount(firstName, lastName, amount, currency);
        } else {
            // Withdraw from regular account
            success = dbFunctions.withdraw(firstName, lastName, amount);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Withdrawal successful.");
        } else {
            JOptionPane.showMessageDialog(this, "Insufficient funds for withdrawal.");
        }
    }

    private void transfer() {
        String fromFirstName = JOptionPane.showInputDialog("Enter Sender's First Name:");
        String fromLastName = JOptionPane.showInputDialog("Enter Sender's Last Name:");
        String toFirstName = JOptionPane.showInputDialog("Enter Receiver's First Name:");
        String toLastName = JOptionPane.showInputDialog("Enter Receiver's Last Name:");
        String amountStr = JOptionPane.showInputDialog("Enter Amount to Transfer:");
        double amount = Double.parseDouble(amountStr);

        if (isForeignAccount) {
            // Transfer between foreign accounts
            String currency = JOptionPane.showInputDialog("Enter Currency (AUD, USD, GBP, EUR, JPY):");
            dbFunctions.transferForeign(fromFirstName, fromLastName, toFirstName, toLastName, amount, currency);
        } else {
            // Transfer between regular accounts
            dbFunctions.transfer(fromFirstName, fromLastName, toFirstName, toLastName, amount);
        }
    }

    private void getBalance() {
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");

        if (isForeignAccount) {
            // Get balance for foreign account
            Map<String, Double> balances = dbFunctions.getForeignBalance(firstName, lastName);
            if (balances != null) {
                StringBuilder balanceInfo = new StringBuilder("Foreign Account Balances:\n");
                for (Map.Entry<String, Double> entry : balances.entrySet()) {
                    balanceInfo.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                JOptionPane.showMessageDialog(this, balanceInfo.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Account not found.");
            }
        } else {
            // Get balance for regular account
            double balance = dbFunctions.getBalance(firstName, lastName);
            if (balance >= 0) {
                JOptionPane.showMessageDialog(this, "Balance: " + balance);
            } else {
                JOptionPane.showMessageDialog(this, "Account not found.");
            }
        }
    }

    private void deleteAccount() {
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        String accountType = isForeignAccount ? "Foreign Currency Account" : "Regular NZD Account";

        int confirmation = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete the " + accountType + " for " + firstName + " " + lastName + "?",
            "Delete Account", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (isForeignAccount) {
                ForeignAccount account = dbFunctions.getForeignAccount(firstName, lastName);
                if (account != null && getTotalBalance(account) == 0) {
                    dbFunctions.deleteAccount(firstName, lastName, true);
                    JOptionPane.showMessageDialog(null, "Foreign account deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Account can't be deleted because it's not empty or doesn't exist.");
                }
            } else {
                Account account = dbFunctions.getAccount(firstName, lastName);
                if (account != null && account.getBalance() == 0) {
                    dbFunctions.deleteAccount(firstName, lastName, false);
                    JOptionPane.showMessageDialog(null, "Regular account deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Account can't be deleted because it's not empty or doesn't exist.");
                }
            }
        }
    }

    private double getTotalBalance(ForeignAccount account) {
        double totalBalance = 0;
        Map<String, Double> currencyBalances = account.getCurrencyBalances();
        for (double balance : currencyBalances.values()) {
            totalBalance += balance;
        }
        return totalBalance;
    }
}
