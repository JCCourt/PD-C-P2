// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BankFunctions {

    private FileIO regularAccountFileIO;
    private FileIO foreignAccountFileIO;
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, Double> conversionRates;

    public BankFunctions() {
        this.regularAccountFileIO = new FileIO("accounts.txt");
        this.foreignAccountFileIO = new FileIO("foreignaccounts.txt");
        this.loadAccountsFromFile();
        this.loadForeignAccountsFromFile();
        this.loadConversionRates();
    }

    // ********************
    // Load Data from Files
    // ********************
    // Load Regular Accounts from accounts.txt
    private void loadAccountsFromFile() {
        Map<String, Double> accountBalances = regularAccountFileIO.readRegularAccounts();
        for (Map.Entry<String, Double> entry : accountBalances.entrySet()) {
            Account account = new Account(entry.getKey(), entry.getValue());
            accounts.put(entry.getKey(), account);
        }
    }

    // Load Foreign Accounts from foreignaccounts.txt
    private void loadForeignAccountsFromFile() {
        Map<String, Map<String, Double>> foreignAccountData = foreignAccountFileIO.readForeignAccounts();
        for (Map.Entry<String, Map<String, Double>> entry : foreignAccountData.entrySet()) {
            String name = entry.getKey();
            Map<String, Double> currencies = entry.getValue();

            ForeignAccount account = new ForeignAccount(name);
            for (Map.Entry<String, Double> currencyEntry : currencies.entrySet()) {
                String currency = currencyEntry.getKey();
                Double balance = currencyEntry.getValue();
                account.addNewCurrencyToBalance(currency, balance);
            }
            this.addAccount(account);
        }
    }

    // Load currency conversions from currencies.txt
    private void loadConversionRates() {
        this.conversionRates = foreignAccountFileIO.readConversionRates();
    }

    // **************
    // Create Methods
    // **************
    // Add account
    public void addAccount(Account account) {
        accounts.put(account.getName(), account);
    }

    // Get an account name
    public Account getAccount(String name) {
        return accounts.get(name);
    }

    // Create regular account
    public void createAccount(String name, double initialDeposit) {
        if (!accounts.containsKey(name)) {
            Account newAccount = new Account(name, initialDeposit);
            accounts.put(name, newAccount);
            System.out.println("Account created for " + name + " with initial deposit: " + initialDeposit);
            regularAccountFileIO.addToRegularAccounts(name + " " + initialDeposit);
        } else {
            System.out.println("An account with this name already exists.");
        }
    }

    // Create foreign account
    public void createForeignAccount(String name, double initialDeposit, String currency) {

        if (accounts.containsKey(name)) {
            System.out.println("An account with this name already exists.");
            return;
        }
        Double conversionRateToForeignCurrency = conversionRates.get(currency);
        if (conversionRateToForeignCurrency == null) {
            System.out.println("Unsupported currency" + currency);
            return;
        }

        double initialDepositForeignCurrency = initialDeposit * conversionRateToForeignCurrency;

        ForeignAccount newAccount = new ForeignAccount(name, initialDepositForeignCurrency, currency);
        accounts.put(name, newAccount);
        foreignAccountFileIO.addToForeignAccounts(name + " " + initialDepositForeignCurrency + " " + currency);
    }

    // **************
    // Deposit Methods
    // **************
    // Deposit into regular account
    public void deposit(String name, double amount) {
        Account account = accounts.get(name);
        if (account != null && amount > 0) {
            account.deposit(amount);
            System.out.println("Deposited " + amount + " to " + name + "'s account.");
            System.out.println("Current balance is now " + account.getBalance());

            Map<String, Double> accountBalances = regularAccountFileIO.readRegularAccounts();
            accountBalances.put(name, account.getBalance()); // Update balance
            regularAccountFileIO.overwriteRegularAccounts(accountBalances); // Overwrite file
        } else {
            if (account == null) {
                System.out.println("Account not found.");
            } else {
                if (amount <= 0) {
                    System.out.println("Please enter an amount more than $0");
                }
            }
        }
    }

    // Deposit into foreign account
    public void depositForeignAccount(String name, double amountInNZD, String targetCurrency) {
        Account account = accounts.get(name);
        if (account == null) {
            System.out.println("Account not found, Create foreign account first.");
            return;
        }
        if (!(account instanceof ForeignAccount)) {
            System.out.println("Not foreign account.");
            return;
        }
        if (amountInNZD <= 0) {
            System.out.println("Enter an amount more than $0.");
            return;
        }

        Double conversionRateToForeignCurrency = conversionRates.get(targetCurrency);
        if (conversionRateToForeignCurrency == null) {
            System.out.println("Conversion rate for " + targetCurrency + " not found.");
            return;
        }

        double depositForeignCurrency = amountInNZD * conversionRateToForeignCurrency;

        ForeignAccount foreignAccount = (ForeignAccount) account;
        foreignAccount.deposit(depositForeignCurrency, targetCurrency);

        System.out.println("Deposited " + depositForeignCurrency + " " + targetCurrency + " to " + name + "'s foreign account.");

        foreignAccountFileIO.overwriteForeignAccounts(accounts);
    }

    // ****************
    // Withdraw Methods
    // ****************
    // Withdraw from regular account
    public void withdraw(String name, double amount) {
        Account account = accounts.get(name);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Enter an amount more than $0");
            return;
        }
        if (account.getBalance() < amount) {
            System.out.println("Insufficient funds for withdrawal.");
            return;
        }

        account.withdraw(amount);
        System.out.println("Withdrew " + amount + " from " + name + "'s account.");
        System.out.println("Current balance is now " + account.getBalance());

        Map<String, Double> accountBalances = regularAccountFileIO.readRegularAccounts();
        accountBalances.put(name, account.getBalance());

        regularAccountFileIO.overwriteRegularAccounts(accountBalances);

    }

    // Withdraw from foreign account
    public void withdrawForeignAccount(String name, double amountInSelectedCurrency, String selectedCurrency) {
        Account account = accounts.get(name);
        if (account == null) {
            System.out.println("Account not found. ");
            return;
        }
        if (amountInSelectedCurrency <= 0) {
            System.out.println("Please enter an amount more than $ " + selectedCurrency + ".");
            return;
        }
        if (!(account instanceof ForeignAccount)) {
            System.out.println("This is not a foreign account.");
            return;
        }

        ForeignAccount foreignAccount = (ForeignAccount) account;
        try {
            foreignAccount.withdraw(amountInSelectedCurrency, selectedCurrency); // Assumes that ForeignAccount handles the error
            System.out.println("Withdrew " + amountInSelectedCurrency + " " + selectedCurrency + " from " + name + "'s foreign account.");
            System.out.println("Current balance in " + selectedCurrency + " is now " + foreignAccount.getCurrencyBalances().get(selectedCurrency));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        foreignAccountFileIO.overwriteForeignAccounts(accounts);
    }

    // ****************
    // Transfer Methods
    // ****************
    // Transfer (Pay) a person to and from regular accounts only.
    public void transfer(String fromName, String toName, double amount) {
        Account fromAccount = accounts.get(fromName);
        Account toAccount = accounts.get(toName);

        if (fromAccount != null && toAccount != null) {
            if (fromAccount.getBalance() >= amount) {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
                System.out.println("Transferred " + amount + " from " + fromName + " to " + toName);

                Map<String, Double> accountBalances = regularAccountFileIO.readRegularAccounts();
                accountBalances.put(fromName, fromAccount.getBalance());
                accountBalances.put(toName, toAccount.getBalance());
                regularAccountFileIO.overwriteRegularAccounts(accountBalances);
            } else {
                System.out.println("Insufficient funds in " + fromName + "'s account for transfer.");
            }
        } else {
            System.out.println("One or both accounts not found.");
        }
    }

    // Transfer (Pay) a person to and from foreign accounts only.
    public void transferForeignAccounts(String fromName, String toName, String currency, double amount) {
        if (!(accounts.get(fromName) instanceof ForeignAccount) || !(accounts.get(toName) instanceof ForeignAccount)) {
            System.out.println("One or both accounts do not exist or are not foreign accounts.");
            return;
        }

        ForeignAccount fromAccount = (ForeignAccount) accounts.get(fromName);
        ForeignAccount toAccount = (ForeignAccount) accounts.get(toName);

        Map<String, Double> fromCurrencyBalances = fromAccount.getCurrencyBalances();
        Map<String, Double> toCurrencyBalances = toAccount.getCurrencyBalances();

        Double fromCurrentBalance = fromCurrencyBalances.getOrDefault(currency, 0.0);
        Double toCurrentBalance = toCurrencyBalances.getOrDefault(currency, 0.0);

        if (fromCurrentBalance < amount) {
            System.out.println("Insufficient funds in " + fromName + "'s account in " + currency);
            return;
        }

        fromCurrencyBalances.put(currency, fromCurrentBalance - amount);
        toCurrencyBalances.put(currency, toCurrentBalance + amount);

        System.out.println("Transferred " + currency + " " + amount + " from " + fromName + " to " + toName);

        foreignAccountFileIO.overwriteForeignAccounts(accounts);
    }

    // *******************
    // Get Balance Methods
    // *******************
    // Get balance from regular account
    public double getBalance(String name) {
        Account account = accounts.get(name);
        if (account != null) {
            return account.getBalance();
        } else {
            System.out.println("Account not found.");
            return 0;
        }
    }

    // Get balance from foreign account
    public Map<String, Double> getBalanceForeign(String name) {
        Account account = accounts.get(name);
        if (account == null) {
            System.out.println("Account not found.");
            // Collections class to return emptymap if null
            return Collections.emptyMap();
        }
        if (!(account instanceof ForeignAccount)) {
            System.out.println("The selected account is not a foreign account.");
            // Collections class to return emptymap if account not foreign account
            return Collections.emptyMap();
        }
        ForeignAccount foreignAccount = (ForeignAccount) account;
        return foreignAccount.getCurrencyBalances();
    }

    // *******************
    // Delete Account
    // *******************
    // Delete user account
    public boolean deleteAccount(String name, boolean isForeign) {
        Account account = accounts.get(name);

        if (account == null) {
            System.out.println("Account not found.");
            return false;
        }
        if (isForeign && !(account instanceof ForeignAccount)) {
            System.out.println("The selected account is not a foreign account.");
            return false;
        } else if (!isForeign && account instanceof ForeignAccount) {
            System.out.println("The selected account is not a regular account.");
            return false;
        }
        // Check if account is empty
        double balance;
        if (account instanceof ForeignAccount) {
            //return values from getCurrencyBalances -> converts to streams double object -> convert doublt to primitage -> sum to get totals
            balance = ((ForeignAccount) account).getCurrencyBalances().values().stream().mapToDouble(Double::doubleValue).sum();
        } else {
            balance = account.getBalance();
        }
        if (balance > 0) {
            System.out.println("Account cant be deleted because balance not empty.");
            return false;
        }

        accounts.remove(name);

        // Update corresponding deleted account from .txt file
        if (isForeign) {
            // Update foreignaccounts.txt
            Map<String, Account> remainingAccounts = new HashMap<>(accounts);
            foreignAccountFileIO.overwriteForeignAccounts(remainingAccounts);
        } else {
            // Update accounts.txt
            Map<String, Double> accountBalances = new HashMap<>();
            for (Account acc : accounts.values()) {
                if (!(acc instanceof ForeignAccount)) {
                    accountBalances.put(acc.getName(), acc.getBalance());
                }
            }
            regularAccountFileIO.overwriteRegularAccounts(accountBalances);
        }

        return true;
    }

}
