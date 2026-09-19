package com.gdb.exceptions;

// Step 2 - Thrown when an operation is attempted on a suspended or closed account.
public class InactiveAccountException extends AccountException {
    public InactiveAccountException(String message) {
        super(message);
    }
}
