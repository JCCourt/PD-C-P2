package bankingapplication;

import java.util.HashMap;
import java.util.Map;

// Foreign account extends account class to superclass the name from account
public class ForeignAccount extends Account {

    // Hashmap used for balances using String and Double to store currency text and balance
    private Map<String, Double> currencyBalances;

    // Constructor 1
    public ForeignAccount(String name) {
        super(name);
        this.currencyBalances = new HashMap<>();
    }

    // Constructor 2
    public ForeignAccount(String name, double initialBalance, String currency) {
        super(name, initialBalance);
        this.currencyBalances = new HashMap<>();
        this.currencyBalances.put(currency, initialBalance);
    }

    // Return total foreign currency balances for account method
    public Map<String, Double> getCurrencyBalances() {
        return this.currencyBalances;
    }

    // Deposit currency symbol + amount into account
    public void deposit(double amount, String currency) {
        //.put() to insert new mapping of currency and amount 
        currencyBalances.put(currency, currencyBalances.getOrDefault(currency, 0.0) + amount);
    }

    // Withdraw curre
    public void withdraw(double amount, String currency) throws IllegalArgumentException {
        Double currentBalance = currencyBalances.get(currency);
        if (currentBalance == null || currentBalance < amount) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal in " + currency);
        }
        currencyBalances.put(currency, currentBalance - amount);
    }

    // Add new currency to foreign account balance if not already in the balance
    public void addNewCurrencyToBalance(String currency, Double balance) {
        currencyBalances.merge(currency, balance, Double::sum);
    }
}
