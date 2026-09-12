import java.util.ArrayList;
import java.util.List;

/**
 * GreenLeaf Bank — Legacy BankAccount class
 *
 * This class is intentionally messy.
 * It mixes account state, validation, persistence, notification,
 * statement formatting, and interest calculation all in one place.
 * Refactor this across by implementing the lab tasks onward.
 */

/* one-line "job description" for what BankAccount should be
responsible for
BankAccount holds a single account's state 
(number, holder, type, status, balance) 
and applies deposits and withdrawals to that balance, 
rejecting any that aren't valid — nothing else.
*/
/*
Doing the section 1 , left me with creating 5 files with there own functional part for the whole BankAccount System.
This makes testing easier because each responsibility can be tested independently without setting up the entire account system.
Dependencies like the database and email service can also be mocked, making tests faster and more reliable.
 */

public class BankAccount {

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String status;
    private String accountType; // "Savings" or "Current"

    // Every deposit/withdrawal gets logged here as a plain string.
    private List<String> transactionLog = new ArrayList<>();

    public BankAccount(int accountNumber, String name, int age, double balance, String accountType) {

        // Validation logic mixed directly into the constructor
        if (age < 18) {
            System.out.println("Age was below 18, correcting to 18");
            age = 18;
        }

        double minimumBalance = accountType.equals("Savings") ? 500.0 : 1000.0;
        if (balance < minimumBalance) {
            System.out.println("Initial balance below minimum, correcting to " + minimumBalance);
            balance = minimumBalance;
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = "Active";
    }

    // ----------------------------------------------------
    // Account operations
    // ----------------------------------------------------

    public boolean deposit(double amount) {

        if (!status.equals("Active")) {
            System.out.println("Account is not active");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Invalid deposit amount");
            return false;
        }

        balance += amount;

        // Logging responsibility, baked directly into deposit()
        transactionLog.add("DEPOSIT: Rs. " + amount + " | New balance: " + balance);

        return true;
    }

    public boolean withdraw(double amount) {

        if (!status.equals("Active")) {
            System.out.println("Account is not active");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }

        double minimumBalance = accountType.equals("Savings") ? 500.0 : 1000.0;
        if (balance - amount < minimumBalance) {
            System.out.println("Withdrawal would breach minimum balance");
            return false;
        }

        balance -= amount;

        transactionLog.add("WITHDRAW: Rs. " + amount + " | New balance: " + balance);

        return true;
    }

    // ----------------------------------------------------
    // Getters
    // ----------------------------------------------------

    public int getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
    public String getAccountType() { return accountType; }
    public List<String> getTransactionLog() {return new ArrayList<>(transactionLog);}

}
