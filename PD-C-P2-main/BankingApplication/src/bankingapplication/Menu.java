// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

import java.util.Map;
import java.util.Scanner;

public class Menu {

    private Scanner scanner;
    private BankFunctions bankFunctions;
    private LoanCalculator loanCalculator;
    private MortgageCalculator mortgageCalculator;
    private DeleteAccount deleteAccount;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.bankFunctions = new BankFunctions();
        this.loanCalculator = new LoanCalculator("accounts.txt");
        this.mortgageCalculator = new MortgageCalculator();
        this.deleteAccount = new DeleteAccount(bankFunctions);
    }

    public void displayMenu() {
        while (true) {
            // Base output for the continous CUI output.
            System.out.println("1. Create User Account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Balance Inquiry\n6. Loan Calculator\n7. Mortgage Calculator\n8. Currency Exchange Calculator\n9. Delete User Account\nx. Exit Application\n");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "1":
                    // Create User Accounts      
                    System.out.println("Do you want to create a \n(1) Regular NZD Account \n(2) Foreign Currency Account");
                    String accountType = scanner.nextLine();
                    System.out.println("Enter Full Name: ");
                    String name = scanner.nextLine();
                    System.out.println("Initial deposit amount (NZD): ");
                    double initialDeposit = Double.parseDouble(scanner.nextLine());

                    if (null == accountType) {
                        System.out.println("Invalid account type selected.");
                    } else {
                        switch (accountType) {
                            case "1":
                                bankFunctions.createAccount(name, initialDeposit);
                                break;
                            case "2":
                                System.out.println("Enter currency (AUD, USD, GBP, EUR, JPY): ");
                                String currency = scanner.nextLine().toUpperCase();
                                // use .matches() to check if scanner input matches which returns a boolean
                                if (currency.matches("AUD|USD|GBP|EUR|JPY")) {
                                    bankFunctions.createForeignAccount(name, initialDeposit, currency);
                                } else {
                                    System.out.println("Invalid currency selected, enter one of the following currencies: AUD, USD, GBP, EUR, JPY.");
                                    break;
                                }
                                break;
                            default:
                                System.out.println("Invalid account type selected.");
                                break;
                        }
                    }
                    break;
                case "2":
                    // Deposit in regular or foreign accounts
                    System.out.println("Enter Full name: ");
                    name = scanner.nextLine();
                    System.out.println("Do you want to deposit into \n(1) Regular NZD Account \n(2) Foreign Currency Account");
                    String depositChoice = scanner.nextLine();

                    try {
                        if (null == depositChoice) {
                            System.out.println("Invalid option selected.");
                        } else {
                            switch (depositChoice) {
                                case "1":
                                    System.out.println("Deposit amount (NZD): ");
                                    double depositAmount = Double.parseDouble(scanner.nextLine()); // Parse to double
                                    bankFunctions.deposit(name, depositAmount);
                                    break;
                                case "2":
                                    System.out.println("Enter currency (AUD, USD, GBP, EUR, JPY): ");
                                    String currency = scanner.nextLine().toUpperCase();
                                    // use .matches() to check if scanner input matches which returns a boolean
                                    if (!currency.matches("AUD|USD|GBP|EUR|JPY")) {
                                        System.out.println("Invalid currency selected, enter one of the following currencies: AUD, USD, GBP, EUR, JPY.");
                                    } else {
                                        System.out.println("Deposit amount in (NZD): ");
                                        depositAmount = Double.parseDouble(scanner.nextLine()); // Parse to double
                                        bankFunctions.depositForeignAccount(name, depositAmount, currency);
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid option selected.");
                                    break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount entered, enter a number only.");
                    }
                    break;
                case "3":
                    // Withdraw from regular or foreign accounts  
                    System.out.println("Enter Full Name: ");
                    name = scanner.nextLine();
                    System.out.println("Withdraw from: \n(1) Regular NZD Account Balance \n(2) Foreign Currency Account");
                    accountType = scanner.nextLine();
                    if (null == accountType) {
                        System.out.println("Invalid option selected.");
                    } else {
                        switch (accountType) {
                            case "1":
                                System.out.println("Withdraw amount (NZD): ");
                                double withdrawAmount = scanner.nextDouble();
                                scanner.nextLine();
                                bankFunctions.withdraw(name, withdrawAmount);
                                break;
                            case "2":
                                System.out.println("Enter currency (AUD, USD, GBP, EUR, JPY): ");
                                String currency = scanner.nextLine().toUpperCase();
                                if (!currency.matches("AUD|USD|GBP|EUR|JPY")) {
                                    System.out.println("Invalid currency selected, enter one of the following currencies: AUD, USD, GBP, EUR, JPY.");
                                } else {
                                    // Using instanceof to check if the object is of given type resulting in boolean.
                                    if (bankFunctions.getAccount(name) instanceof ForeignAccount) {
                                        ForeignAccount foreignAccount = (ForeignAccount) bankFunctions.getAccount(name);
                                        Map<String, Double> currencyBalances = foreignAccount.getCurrencyBalances();
                                        Double currentBalance = currencyBalances.getOrDefault(currency, 0.0);
                                        System.out.println("Current balance in " + currency + ": " + currentBalance);
                                    } else {
                                        System.out.println("Account not found or not a foreign account.");
                                        break;
                                    }
                                    System.out.println("Withdraw amount (" + currency + "): ");
                                    double withdrawAmountForeign = scanner.nextDouble();
                                    scanner.nextLine();
                                    bankFunctions.withdrawForeignAccount(name, withdrawAmountForeign, currency);
                                }
                                break;
                            default:
                                System.out.println("Invalid option selected.");
                                break;
                        }
                    }
                    break;
                case "4":
                    // Transfer from regular to regular accounts or foreign to foreign accounts (cross account types not possible.)
                    System.out.println("Select transfer type:\n(1) Regular NZD Account Transfer \n(2) Foreign Currency Account Transfer");
                    String transferType = scanner.nextLine();

                    if (null == transferType) {
                        System.out.println("Invalid selection, choose a transfer type.");
                    } else {
                        switch (transferType) {
                            case "1": {
                                // Regular account transfer
                                System.out.println("From Full Name: ");
                                String fromName = scanner.nextLine();
                                System.out.println("To Full Name: ");
                                String toName = scanner.nextLine();
                                System.out.println("Transfer Amount (NZD): ");
                                double transferAmount = scanner.nextDouble();
                                scanner.nextLine();
                                bankFunctions.transfer(fromName, toName, transferAmount);
                                break;
                            }
                            case "2": {
                                // Foreign currency transfer
                                System.out.println("From Full Name: ");
                                String fromName = scanner.nextLine();
                                System.out.println("To Full Name: ");
                                String toName = scanner.nextLine();
                                System.out.println("Enter currency (AUD, USD, GBP, EUR, JPY): ");
                                String currency = scanner.nextLine();
                                System.out.println("Transfer Amount (in selected currency): ");
                                double transferAmount = scanner.nextDouble();
                                scanner.nextLine();
                                bankFunctions.transferForeignAccounts(fromName, toName, currency, transferAmount);
                                System.out.println("Foreign currency transfer complete.");
                                break;
                            }
                            default:
                                System.out.println("Invalid selection, choose a transfer type.");
                                break;
                        }
                    }
                    break;
                case "5":
                    // Check balance in regular accounts or foreign accounts. 
                    System.out.println("Select the type of account balance to check: \n(1) Regular NZD Account Balance \n(2) Foreign Currency Account Balance");
                    String balanceType = scanner.nextLine();
                    System.out.println("Enter Full Name: ");
                    name = scanner.nextLine();

                    if (null == balanceType) {
                        System.out.println("Invalid selection, choose an account balance type.");
                    } else {
                        switch (balanceType) {
                            case "1":
                                // Regular account balance
                                double balance = bankFunctions.getBalance(name);
                                System.out.printf("Current balance: %.2f NZD\n", balance); //round balance to 2dp
                                break;
                            case "2":
                                // Foreign account balance
                                Account account = bankFunctions.getAccount(name);
                                // Using instanceof to check if the object is of given type resulting in boolean.
                                if (!(account instanceof ForeignAccount)) {
                                    System.out.println("The specified account is not a foreign account or does not exist.");
                                } else {
                                    //Type casting to Foreign Account
                                    ForeignAccount foreignAccount = (ForeignAccount) account;
                                    Map<String, Double> currencyBalances = foreignAccount.getCurrencyBalances();

                                    if (currencyBalances.isEmpty()) {
                                        System.out.println("No Foreign Account balances found.");
                                    } else {
                                        System.out.println("Foreign Account Currency balances:");
                                        // entry used to create set of elements from hashmap
                                        for (Map.Entry<String, Double> entry : currencyBalances.entrySet()) {
                                            // getKey of currency convert to string -> getValue of balance convert to get float and convert to 2dp
                                            System.out.printf("%s: %.2f\n", entry.getKey(), entry.getValue());
                                            break;
                                        }
                                    }
                                }
                                break;
                            default:
                                System.out.println("Invalid selection. Please choose a valid account balance type.");
                                break;
                        }
                    }
                    break;

                case "6":
                    // Call loan calculator method in LoanCalculator.
                    loanCalculator.calculateMoneyLoan();
                    break;
                case "7":
                    // Call mortgage calculator method in CalculatoreMortgage.
                    mortgageCalculator.calculateMortage();
                    break;
                case "8":
                    // Call calculate exchange method in CurrencyExchange.
                    CurrencyExchange currencyExchange = new CurrencyExchange("currencies.txt");
                    currencyExchange.calculateExchange();
                    break;
                case "9":
                    // Call delete account method in Delete Account. Users must have empty balance in either account to delete the corrosponding account they want to delete.
                    deleteAccount.deleteAccount();
                    break;
                case "X":
                    // Log out of app
                    System.out.println("Exiting app...");
                    System.exit(0);
                default:
                    // Error checking for invalid input.
                    System.out.println("Invalid option, try again.");
                    break;
            }
            scanner.nextLine();
        }
    }
}
