// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

import java.util.Map;
import java.util.Scanner;

public class LoanCalculator {

    private FileIO fileIO;

    public LoanCalculator(String fileName) {
        this.fileIO = new FileIO(fileName);
    }

    public void calculateMoneyLoan() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name to check eligibility: ");
        String name = scanner.nextLine();

        // Map to read regular account balances to check if persons balance is enough
        Map<String, Double> accountBalances = fileIO.readRegularAccounts();
        if (accountBalances.containsKey(name) && accountBalances.get(name) >= 1000) {
            System.out.println("They are eligible to get a loan.");
            System.out.print("Enter the amount they want to take out: ");
            double loanAmountRequested = scanner.nextDouble();

            System.out.print("Enter the annual interest rate (as percentage, not decimal): ");
            double annualInterestRate = scanner.nextDouble();

            System.out.print("Enter the loan length (years): ");
            int loanLength = scanner.nextInt();

            double monthlyInterestRate = (annualInterestRate / 100) / 12;
            int totalPayments = loanLength * 12;

            double monthlyPayment = loanAmountRequested
                    // Math.pow to calculate power of a given number.
                    * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPayments)) / (Math.pow(1 + monthlyInterestRate, totalPayments) - 1);

            double totalRepayment = monthlyPayment * totalPayments;

            System.out.printf("Monthly payment: $%.2f\n", monthlyPayment);
            System.out.printf("Total repayment amount: $%.2f\n", totalRepayment);
        } else {
            System.out.println("They must have at least $1000 in account to qualify for a loan.");
        }
    }
}
