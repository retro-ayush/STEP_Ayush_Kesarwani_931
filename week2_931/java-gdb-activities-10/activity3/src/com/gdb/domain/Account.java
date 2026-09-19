package com.gdb.domain;

/**
 * Account - Enhanced bank account with PIN authentication, age validation, and status transitions.
 * Starts from the completed Activity 1 Account, with the Activity 3 upgrades applied.
 */
public class Account {
    private String accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    // Step 1 - The customer's 4-digit security PIN.
    private String pin;

    public Account(String accountNumber, String name, int age, double balance, String accountType, String status, String pin) {
        // Step 2 - Validate the inputs BEFORE assigning any field.
        if (age < 18) {
            throw new IllegalArgumentException("Customer age must be 18 or above");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be 4 digits");
        }
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.pin = pin;
    }

    public boolean validatePin(String enteredPin) {
        // Step 3 - True only when the entered PIN matches the stored one.
        if (enteredPin == null || enteredPin.isEmpty()) {
            return false;
        }
        return enteredPin.equals(pin);
    }

    public boolean changePin(String oldPin, String newPin) {
        // Step 4 - Change the PIN only after the old one is verified.
        if (!validatePin(oldPin)) {
            return false;
        }
        if (newPin == null || !newPin.matches("\\d{4}")) {
            return false;
        }
        this.pin = newPin;
        return true;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount, String enteredPin) {
        // Step 5 - PIN-protected withdrawal (replaces the Activity 1 withdraw).
        if (!validatePin(enteredPin)) {
            return false;
        }
        if (!"ACTIVE".equalsIgnoreCase(status)) {
            return false;
        }
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public void suspend() {
        // Step 6.1
        this.status = "SUSPENDED";
    }

    public void activate() {
        // Step 6.2
        this.status = "ACTIVE";
    }

    public void close() {
        // Step 6.3
        this.status = "CLOSED";
    }

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Account Type: " + accountType);
        System.out.println("Status: " + status);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getStatus() { return status; }
}
