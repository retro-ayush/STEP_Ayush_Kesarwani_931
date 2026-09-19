package com.gdb.exceptions;

// Step 2 - Thrown when the account does not have enough balance for a withdrawal.
public class InsufficientBalanceException extends AccountException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
