package com.gdb.tests;

import com.gdb.domain.Account;

public class TestAccount {
    public static void main(String[] args) {
        System.out.println("=== Activity 4: Enhanced Account Test Suite ===");

        // NOTE: If you completed Activity 3 successfully, paste your working Account.java code into com.gdb.domain.

        // Step 1 - Test Underage Customer Rejection (age < 18 throws IllegalArgumentException)
        boolean test1 = false;
        try {
            new Account("ACC9999", "Minor Customer", 16, 5000.0, "SAVINGS", "ACTIVE", "1234");
        } catch (IllegalArgumentException e) {
            test1 = true;
        }
        System.out.println("Test 1 (Underage Customer Rejection): " + (test1 ? "[PASS]" : "[FAIL]"));

        Account acc = new Account("ACC1001", "Rajesh Sharma", 28, 5000.0, "SAVINGS", "ACTIVE", "1234");

        // Step 2 - Test Wrong PIN Rejection on Withdrawal (Verify returns false and balance unchanged)
        boolean wrongPin = acc.withdraw(1000.0, "9999");
        boolean test2 = !wrongPin && acc.getBalance() == 5000.0;
        System.out.println("Test 2 (Wrong PIN Rejection): " + (test2 ? "[PASS]" : "[FAIL]"));

        // Step 3 - Test Correct PIN Withdrawal (Verify returns true and balance decreases)
        boolean rightPin = acc.withdraw(1000.0, "1234");
        boolean test3 = rightPin && acc.getBalance() == 4000.0;
        System.out.println("Test 3 (Correct PIN Withdrawal): " + (test3 ? "[PASS]" : "[FAIL]"));

        // Step 4 - Test PIN Change Functionality (Change PIN, verify old PIN fails, new PIN succeeds)
        boolean changed = acc.changePin("1234", "5678");
        boolean oldPinFails = !acc.withdraw(1000.0, "1234");
        boolean newPinWorks = acc.withdraw(1000.0, "5678");
        boolean test4 = changed && oldPinFails && newPinWorks && acc.getBalance() == 3000.0;
        System.out.println("Test 4 (PIN Change & Old PIN Invalidation): " + (test4 ? "[PASS]" : "[FAIL]"));

        // Step 5 - Test Suspended Account Block (Suspend account, verify withdrawal blocked)
        acc.suspend();
        boolean suspendedWithdraw = acc.withdraw(1000.0, "5678");
        boolean test5 = !suspendedWithdraw && acc.getBalance() == 3000.0;
        System.out.println("Test 5 (Suspended Account Block): " + (test5 ? "[PASS]" : "[FAIL]"));

        // Step 6 - Test Reactivation & Success (Activate account, verify withdrawal succeeds)
        acc.activate();
        boolean reactivatedWithdraw = acc.withdraw(1000.0, "5678");
        boolean test6 = reactivatedWithdraw && acc.getBalance() == 2000.0;
        System.out.println("Test 6 (Reactivation & Success): " + (test6 ? "[PASS]" : "[FAIL]"));

        System.out.println("All Enhanced Account tests passed!");
    }
}
