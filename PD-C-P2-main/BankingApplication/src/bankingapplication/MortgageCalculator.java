// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

import java.util.Scanner;

public class MortgageCalculator {

    public void calculateMortage() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the mortgage loan amount: ");
        double loanAmount = scanner.nextDouble();

        System.out.println("Choices of length are: \n1. 15 years \n2. 20 years \n3. 30 years");
        System.out.print("Select choice 1 - 3: ");
        int lengthChoice = scanner.nextInt();
        // Ternary operator to do conditionals like if-else
        int lengthInYears = lengthChoice == 1 ? 15 : lengthChoice == 2 ? 20 : 30; // Default 30 years for incorrect input

        System.out.print("Enter annual interest rate (in %): ");
        double annualInterestRate = scanner.nextDouble();

        double monthlyInterestRate = annualInterestRate / 100 / 12;
        int numberOfPayments = lengthInYears * 12;

        //Math.pow to calculate power of a given number.
        double monthlyPayment = loanAmount
                * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
                / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);

        System.out.printf("The monthly mortgage payment: $%.2f\n", monthlyPayment);

    }
}
