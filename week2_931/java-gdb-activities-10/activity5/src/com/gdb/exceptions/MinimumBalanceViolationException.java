package com.gdb.exceptions;

// Step 2 - Thrown when a withdrawal would breach the minimum balance requirement.
public class MinimumBalanceViolationException extends AccountException {
    public MinimumBalanceViolationException(String message) {
        super(message);
    }
}
