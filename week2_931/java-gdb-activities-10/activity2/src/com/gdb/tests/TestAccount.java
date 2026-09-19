package com.gdb.tests;

import com.gdb.domain.Account;

public class TestAccount {
    public static void main(String[] args) {
        System.out.println("=== Activity 2: Test Account Suite ===");

        // NOTE: If you completed Activity 1 successfully, paste your working Account.java code into com.gdb.domain.

        Account acc = new Account("ACC1001", "Rajesh Sharma", 28, 5000.0, "SAVINGS", "ACTIVE");

        // Step 1 - Test Initial Balance (Assert balance == 5000.0)
        boolean test1 = acc.getBalance() == 5000.0;
        System.out.println("Test 1 (Initial Balance 5000.0): " + (test1 ? "[PASS]" : "[FAIL]"));

        // Step 2 - Test Valid Deposit (Deposit 2000.0 -> Assert balance == 7000.0)
        boolean deposited = acc.deposit(2000.0);
        boolean test2 = deposited && acc.getBalance() == 7000.0;
        System.out.println("Test 2 (Deposit 2000.0 -> Balance 7000.0): " + (test2 ? "[PASS]" : "[FAIL]"));

        // Step 3 - Test Negative Deposit (Deposit -500.0 -> Assert returns false and balance stays 7000.0)
        boolean negDeposited = acc.deposit(-500.0);
        boolean test3 = !negDeposited && acc.getBalance() == 7000.0;
        System.out.println("Test 3 (Negative Deposit -> Rejected): " + (test3 ? "[PASS]" : "[FAIL]"));

        // Step 4 - Test Valid Withdrawal (Withdraw 3000.0 -> Assert balance == 4000.0)
        boolean withdrawn = acc.withdraw(3000.0);
        boolean test4 = withdrawn && acc.getBalance() == 4000.0;
        System.out.println("Test 4 (Withdraw 3000.0 -> Balance 4000.0): " + (test4 ? "[PASS]" : "[FAIL]"));

        // Step 5 - Test Exceeding Withdrawal (Withdraw 10000.0 -> Assert returns false and balance stays 4000.0)
        boolean overWithdrawn = acc.withdraw(10000.0);
        boolean test5 = !overWithdrawn && acc.getBalance() == 4000.0;
        System.out.println("Test 5 (Exceeding Withdrawal -> Rejected): " + (test5 ? "[PASS]" : "[FAIL]"));

        // Step 6 - Test Negative Withdrawal (Withdraw -100.0 -> Assert returns false and balance stays 4000.0)
        boolean negWithdrawn = acc.withdraw(-100.0);
        boolean test6 = !negWithdrawn && acc.getBalance() == 4000.0;
        System.out.println("Test 6 (Negative Withdrawal -> Rejected): " + (test6 ? "[PASS]" : "[FAIL]"));

        System.out.println("All Account tests completed successfully!");
    }
}