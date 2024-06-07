package bankingapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyExchangeGUI extends JButton {

    private JFrame parentFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Map<String, Double> conversionRates = new HashMap<>();
    private JTextField amountField;

    public CurrencyExchangeGUI(JFrame parentFrame, JPanel cardPanel, CardLayout cardLayout) {
        super("Currency Exchange");
        this.parentFrame = parentFrame;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent e) {
        if (null == cardPanel.getClientProperty("CurrencyPanelInitialized")) {
            JPanel currencyExchangePanel = new JPanel(new BorderLayout());
            cardPanel.add(currencyExchangePanel, "CurrencyExchange");
            cardPanel.putClientProperty("CurrencyPanelInitialized", true);
            SwingUtilities.invokeLater(() -> {
                fetchCurrencyRatesFromDB(); // Fetch rates asynchronously
                setupCurrencyExchangeGUI(currencyExchangePanel);
                cardLayout.show(cardPanel, "CurrencyExchange");
            });
        } else {
            cardLayout.show(cardPanel, "CurrencyExchange");
        }
    }

    private void setupCurrencyExchangeGUI(JPanel panel) {
        panel.removeAll();
        if (conversionRates.isEmpty()) {
            panel.add(new JLabel("No currency rates available."), BorderLayout.CENTER);
        } else {
            amountField = new JTextField("Enter amount in NZD", 15);
            JComboBox<String> currencyComboBox = new JComboBox<>(conversionRates.keySet().toArray(new String[0]));
            JTextArea detailsTextArea = new JTextArea(10, 30);
            detailsTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(detailsTextArea);

            currencyComboBox.addActionListener(e -> updateTextResults(detailsTextArea, (String) currencyComboBox.getSelectedItem(), amountField.getText()));
            panel.add(amountField, BorderLayout.NORTH);
            panel.add(currencyComboBox, BorderLayout.CENTER);
            panel.add(scrollPane, BorderLayout.SOUTH);
        }
        panel.revalidate();
        panel.repaint();
    }

    public void updateExchangeRates() {
        System.out.println("Attempting to update currency exchange rates...");
        try {
            // Existing code to fetch and update rates...
            System.out.println("Exchange rates updated successfully.");
        } catch (Exception e) {
            System.out.println("Failed to update exchange rates: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateTextResults(JTextArea textArea, String selectedCurrency, String amountText) {
        try {
            double amount = Double.parseDouble(amountText);
            Double exchangeRate = conversionRates.get(selectedCurrency);
            Double reverseExchangeRate = 1 / exchangeRate;
            double convertedAmountBeforeFee = amount * exchangeRate;
            double finalConvertedAmountAfterFee = convertedAmountBeforeFee * 0.95;

            String outputText = String.format(
                    "Exchange rate to %s: %.4f\nExchange rate back to NZD: %.4f\n"
                    + "Amount in %s before fees: %.2f\nAmount received in %s after 5%% bank fee: %.2f",
                    selectedCurrency, exchangeRate, reverseExchangeRate, selectedCurrency, convertedAmountBeforeFee,
                    selectedCurrency, finalConvertedAmountAfterFee
            );
            textArea.setText(outputText);
        } catch (NumberFormatException ex) {
            textArea.setText("Please enter a valid number.");
        }
    }

    private void fetchCurrencyRatesFromDB() {
        String query = "SELECT CURRENCY, RATE FROM APP.CURRENCIES";
        System.out.println("Running query: " + query);
        try (Connection conn = DBManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                String currency = rs.getString("CURRENCY");
                double rate = rs.getDouble("RATE");
                conversionRates.put(currency, rate);
                System.out.println("Fetched " + currency + " at rate " + rate);
            }
            if (!found) {
                System.out.println("No rates fetched, check database and query.");
            }
        } catch (SQLException e) {
            System.err.println("SQLException during fetchCurrencyRatesFromDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
