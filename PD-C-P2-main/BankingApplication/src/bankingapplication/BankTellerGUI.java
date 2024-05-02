package bankingapplication;

import javax.swing.*;
import java.awt.*;

public class BankTellerGUI extends JFrame {
    public BankTellerGUI() {
        setTitle("Bank Teller Application");
        setSize(800, 500);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel buttons 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        JButton homeButton = new JButton("Home");
        JButton accountsButton = new JButton("Accounts");
        JButton currencyExchangeButton = new JButton("Currency Exchange");
        JButton loanCalculatorButton = new JButton("Loan Calculator");
        JButton mortgageCalculatorButton = new JButton("Mortgage Calculator");
        
        buttonPanel.add(homeButton);
        buttonPanel.add(accountsButton);
        buttonPanel.add(currencyExchangeButton);
        buttonPanel.add(loanCalculatorButton);
        buttonPanel.add(mortgageCalculatorButton);

        // Adding button panel to the bottom of the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankTellerGUI().setVisible(true);
        });
    }
}