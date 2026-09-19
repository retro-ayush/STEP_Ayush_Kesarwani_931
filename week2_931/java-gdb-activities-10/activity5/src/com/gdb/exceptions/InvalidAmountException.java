package com.gdb.exceptions;

// Step 2 - Thrown when a deposit or withdrawal amount is zero or negative.
public class InvalidAmountException extends AccountException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
