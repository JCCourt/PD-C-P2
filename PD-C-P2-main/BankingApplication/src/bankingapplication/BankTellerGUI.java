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

        // Home Panel
        JPanel homePanel = new JPanel();
        homePanel.add(new JLabel("Welcome to the Bank Teller Application"));
        cardPanel.add(homePanel, "Home");

        // Accounts Panel
        AccountsGUI accountsPanel = new AccountsGUI();
        cardPanel.add(accountsPanel, "Accounts");
        
        add(cardPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton homeButton = new JButton("Home");
        JButton accountsButton = new JButton("Accounts");
        JButton currencyExchangeButton = new CurrencyExchangeGUI(this, cardPanel, cardLayout);
        JButton loanCalculatorButton = new LoanCalculatorGUI(this, cardPanel, cardLayout);
        JButton mortgageCalculatorButton = new MortgageCalculatorGUI(this, cardPanel, cardLayout);

        buttonPanel.add(homeButton);
        buttonPanel.add(accountsButton);
        buttonPanel.add(currencyExchangeButton);
        buttonPanel.add(loanCalculatorButton);
        buttonPanel.add(mortgageCalculatorButton);

        add(buttonPanel, BorderLayout.SOUTH);

        homeButton.addActionListener(e -> {
            accountsPanel.resetView();  // Reset AccountsGUI home button pressed
            cardLayout.show(cardPanel, "Home");
        });

        accountsButton.addActionListener(e -> cardLayout.show(cardPanel, "Accounts"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankTellerGUI().setVisible(true));
    }
}
