package bankingapplication;

import javax.swing.*;
import java.awt.*;

public class LoanCalculatorGUI extends JButton {

    private JFrame parentFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public LoanCalculatorGUI(JFrame parentFrame, JPanel cardPanel, CardLayout cardLayout) {
        super("Loan Calculator");
        this.parentFrame = parentFrame;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        addActionListener(e -> showLoanCalculatorPanel());
    }

    private void showLoanCalculatorPanel() {
        if (null == cardPanel.getClientProperty("LoanCalculatorPanelInitialized")) {
            JPanel loanCalculatorPanel = createLoanCalculatorPanel();
            cardPanel.add(loanCalculatorPanel, "LoanCalculator");
            cardPanel.putClientProperty("LoanCalculatorPanelInitialized", true);
        }
        cardLayout.show(cardPanel, "LoanCalculator");
    }

    private JPanel createLoanCalculatorPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2));

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField(10);

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField(10);

        JLabel loanAmountLabel = new JLabel("Loan Amount:");
        JTextField loanAmountField = new JTextField(10);

        JLabel interestRateLabel = new JLabel("Annual Interest Rate (%):");
        JTextField interestRateField = new JTextField(10);

        JLabel loanLengthLabel = new JLabel("Loan Length (Years):");
        JTextField loanLengthField = new JTextField(10);

        JTextArea resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JButton calculateButton = new JButton("Calculate");

        calculateButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            double balance = new DBFunctions().getBalance(firstName, lastName);

            if (balance >= 1000) {
                try {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    double annualInterestRate = Double.parseDouble(interestRateField.getText());
                    int loanLength = Integer.parseInt(loanLengthField.getText());

                    double monthlyInterestRate = (annualInterestRate / 100) / 12;
                    int totalPayments = loanLength * 12;

                    double monthlyPayment = loanAmount
                            * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPayments))
                            / (Math.pow(1 + monthlyInterestRate, totalPayments) - 1);

                    double totalRepayment = monthlyPayment * totalPayments;

                    resultArea.setText(String.format("Monthly payment: $%.2f\nTotal repayment amount: $%.2f\n", monthlyPayment, totalRepayment));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                resultArea.setText("They must have at least $1000 in account to qualify for a loan.");
            }
        });

        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(loanAmountLabel);
        panel.add(loanAmountField);
        panel.add(interestRateLabel);
        panel.add(interestRateField);
        panel.add(loanLengthLabel);
        panel.add(loanLengthField);
        panel.add(new JLabel()); // Placeholder
        panel.add(calculateButton);
        panel.add(new JLabel()); // Placeholder
        panel.add(scrollPane);

        return panel;
    }
}
