// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

import java.util.Map;
import java.util.Scanner;

public class DeleteAccount {

    private BankFunctions bank;
    private Scanner scanner;

    public DeleteAccount(BankFunctions bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void deleteAccount() {
        System.out.print("Enter the user's full name: ");
        String name = scanner.nextLine();
        System.out.println("Do you want to delete a \n(1) Regular NZD Account \n(2) Foreign Currency Account?");
        String accountType = scanner.nextLine();

        Account account = bank.getAccount(name);
        if (account == null) {
            System.out.println("No account found for the provided name.");
            return;
        }

        boolean foreignDelete = "2".equals(accountType);
        boolean foreignDeleteFound = account instanceof ForeignAccount;

        if (foreignDelete && !foreignDeleteFound) {
            System.out.println("No foreign account for given name.");
            return;
        } else if (!foreignDelete && foreignDeleteFound) {
            System.out.println("The given account is not a regular account.");
            return;
        }

        // Check if the account balance is empty
        // Ternary operator to assign value and to call getTotalBalance method to check if empty
        double balance = foreignDelete ? getTotalBalance((ForeignAccount) account) : account.getBalance();
        if (balance > 0) {
            System.out.println("Account cant be deleted because not empty.");
            return;
        }
        // Perform deletion function in bank class.
        if (bank.deleteAccount(name, foreignDelete)) {
            System.out.println("Account successfully deleted.");
        } else {
            System.out.println("Error while trying to delete account.");
        }
    }

    // Method to check 0 balances for deletion
    private double getTotalBalance(ForeignAccount account) {
        double totalBalance = 0;
        Map<String, Double> currencyBalances = account.getCurrencyBalances();
        for (double balance : currencyBalances.values()) {
            totalBalance += balance;
        }
        return totalBalance;
    }
}
