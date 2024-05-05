package bankingapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MortgageCalculatorGUI extends JButton {

    private JFrame parentFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MortgageCalculatorGUI(JFrame parentFrame, JPanel cardPanel, CardLayout cardLayout) {
        super("Mortgage Calculator");
        this.parentFrame = parentFrame;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent e) {
        //initialise panel for the mortgage calculator once
        if (null == cardPanel.getClientProperty("MortgagePanelInitialized")) {
            JPanel mortgagePanel = createMortgageCalculatorPanel();
            cardPanel.add(mortgagePanel, "MortgageCalculator");
            cardPanel.putClientProperty("MortgagePanelInitialized", true);
        }
        cardLayout.show(cardPanel, "MortgageCalculator");
    }

    private JPanel createMortgageCalculatorPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        JLabel loanAmountLabel = new JLabel("Loan Amount:");
        JTextField loanAmountField = new JTextField(10);

        //length of loan
        JLabel lengthLabel = new JLabel("Length (Years):");
        JComboBox<Integer> lengthChoiceBox = new JComboBox<>(new Integer[]{15, 20, 30});

        //annual interest rate
        JLabel interestRateLabel = new JLabel("Annual Interest Rate (%):");
        JTextField interestRateField = new JTextField(10);

        //displaying results
        JLabel resultLabel = new JLabel("Monthly Payment: ");
        JTextField resultDisplay = new JTextField(10);
        resultDisplay.setEditable(false);  //display field read-only

        //calculate button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> {
            try {
                double loanAmount = Double.parseDouble(loanAmountField.getText());
                int lengthInYears = (int) lengthChoiceBox.getSelectedItem();
                double annualInterestRate = Double.parseDouble(interestRateField.getText());

                double monthlyInterestRate = annualInterestRate / 100 / 12;
                int numberOfPayments = lengthInYears * 12;

                double monthlyPayment = loanAmount
                        * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
                        / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);

                resultDisplay.setText(String.format("$%.2f", monthlyPayment));
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(panel, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //adding components to panel
        panel.add(loanAmountLabel);
        panel.add(loanAmountField);
        panel.add(lengthLabel);
        panel.add(lengthChoiceBox);
        panel.add(interestRateLabel);
        panel.add(interestRateField);
        panel.add(new JLabel());  //placeholder for grid layout adjustment
        panel.add(calculateButton);
        panel.add(new JLabel("Monthly Payment: "));  //label for result
        panel.add(resultDisplay);

        return panel;
    }
}
