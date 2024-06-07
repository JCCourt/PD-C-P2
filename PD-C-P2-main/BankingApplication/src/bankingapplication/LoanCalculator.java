package bankingapplication;

import java.util.Scanner;

public class LoanCalculator {

    private DBFunctions dbFunctions;

    public LoanCalculator() {
        this.dbFunctions = new DBFunctions();
    }

    public void calculateMoneyLoan() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter first name to check eligibility: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name to check eligibility: ");
        String lastName = scanner.nextLine();

        double balance = dbFunctions.getBalance(firstName, lastName);
        if (balance >= 1000) {
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
                    * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPayments))
                    / (Math.pow(1 + monthlyInterestRate, totalPayments) - 1);

            double totalRepayment = monthlyPayment * totalPayments;

            System.out.printf("Monthly payment: $%.2f\n", monthlyPayment);
            System.out.printf("Total repayment amount: $%.2f\n", totalRepayment);
        } else {
            System.out.println("They must have at least $1000 in account to qualify for a loan.");
        }
    }
}
