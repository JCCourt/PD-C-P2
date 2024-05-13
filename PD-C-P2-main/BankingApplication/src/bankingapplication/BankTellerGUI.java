package bankingapplication;

import javax.swing.*;
import java.awt.*;

public class BankTellerGUI extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public BankTellerGUI() {
        setTitle("Bank Teller Application");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        JPanel homePanel = new JPanel();
        homePanel.add(new JLabel("Welcome to the Bank Teller Application"));
        cardPanel.add(homePanel, "Home");

        add(cardPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton homeButton = new JButton("Home");
        JButton accountsButton = new JButton("Accounts");
        JButton currencyExchangeButton = new CurrencyExchangeGUI(this, cardPanel, cardLayout);
        JButton loanCalculatorButton = new JButton("Loan Calculator");
        JButton mortgageCalculatorButton = new MortgageCalculatorGUI(this, cardPanel, cardLayout);

        JPanel homeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(homeButtonPanel, BorderLayout.NORTH);
        homeButtonPanel.add(homeButton);

        homeButton.addActionListener(e -> {
            System.out.println("Home button clicked.");
            cardLayout.show(cardPanel, "Home");
        });

        buttonPanel.add(accountsButton);
        buttonPanel.add(currencyExchangeButton);
        buttonPanel.add(loanCalculatorButton);
        buttonPanel.add(mortgageCalculatorButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> {
                new BankTellerGUI().setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Exception in main: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start the application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
