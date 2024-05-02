// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

// Regular NZD account class 
public class Account {

    private String name;
    private double balance;

    // Constructor 1
    public Account(String name) {
        this(name, 0.0);
    }

    // Constructor 2
    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    // return name method
    public String getName() {
        return name;
    }

    // return balance method
    public double getBalance() {
        return balance;
    }

    // deposit money and increase balance method
    public void deposit(double amount) {
        balance += amount;
    }

    // withdraw money and decrease balance method
    public void withdraw(double amount) {
        balance -= amount;
    }

}
