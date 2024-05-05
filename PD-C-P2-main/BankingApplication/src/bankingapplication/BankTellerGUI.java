package bankingapplication;

import javax.swing.*;
import java.awt.*;

public class BankTellerGUI extends JFrame {
    //card layout to switch between different panels
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public BankTellerGUI() {
        setTitle("Bank Teller Application");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //setup card layout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        //panel for the main/home view
        JPanel homePanel = new JPanel();
        homePanel.add(new JLabel("Welcome to the Bank Teller Application"));
        cardPanel.add(homePanel, "Home");

        //add main card panel center
        add(cardPanel, BorderLayout.CENTER);

        //main panel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton homeButton = new JButton("Home");
        JButton accountsButton = new JButton("Accounts");
        JButton currencyExchangeButton = new JButton("Currency Exchange");
        JButton loanCalculatorButton = new JButton("Loan Calculator");
        JButton mortgageCalculatorButton = new MortgageCalculatorGUI(this, cardPanel, cardLayout);

        //functionality to switch back to home panel
        homeButton.addActionListener(e -> cardLayout.show(cardPanel, "Home"));

        buttonPanel.add(homeButton);
        buttonPanel.add(accountsButton);
        buttonPanel.add(currencyExchangeButton);
        buttonPanel.add(loanCalculatorButton);
        buttonPanel.add(mortgageCalculatorButton);

        //button panel to bottom frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankTellerGUI().setVisible(true));
    }
}
