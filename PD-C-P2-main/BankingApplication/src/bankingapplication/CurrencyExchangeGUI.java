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
                fetchCurrencyRatesFromDB();
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
            String[] currencies = conversionRates.keySet().toArray(new String[0]);
            JComboBox<String> currencyComboBox = new JComboBox<>(currencies);
            JTextArea detailsTextArea = new JTextArea(10, 30);
            detailsTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(detailsTextArea);

            currencyComboBox.addActionListener(e -> UpdateTextResults(detailsTextArea, (String) currencyComboBox.getSelectedItem(), amountField.getText()));
            panel.add(amountField, BorderLayout.NORTH);
            panel.add(currencyComboBox, BorderLayout.CENTER);
            panel.add(scrollPane, BorderLayout.SOUTH);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void UpdateTextResults(JTextArea textArea, String selectedCurrency, String amountText) {
        try {
            double amount = Double.parseDouble(amountText); 
            Double exchangeRate = conversionRates.get(selectedCurrency);
            Double reverseExchangeRate = 1 / exchangeRate;
            double convertedAmountBeforeFee = amount * exchangeRate;
            double finalConvertedAmountAfterFee = convertedAmountBeforeFee * 0.95;

            String outputText = String.format(
                "Exchange rate to %s: %.4f\nExchange rate back to NZD: %.4f\n" +
                "Amount in %s before fees: %.2f\nAmount received in %s after 5%% bank fee: %.2f",
                selectedCurrency, exchangeRate, reverseExchangeRate, selectedCurrency, convertedAmountBeforeFee,
                selectedCurrency, finalConvertedAmountAfterFee
            );
            textArea.setText(outputText);
        } catch (NumberFormatException ex) {
            textArea.setText("Please enter a valid number.");
        }
    }

    private void fetchCurrencyRatesFromDB() {
        String query = "SELECT CURRENCY, RATE FROM PDC.CURRENCIES";
        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                conversionRates.put(rs.getString("CURRENCY"), rs.getDouble("RATE"));
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        System.out.println("Fetched rates: " + conversionRates);
    }
}
