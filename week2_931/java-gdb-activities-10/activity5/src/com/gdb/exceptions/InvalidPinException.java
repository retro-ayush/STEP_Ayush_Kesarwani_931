package com.gdb.exceptions;

// Step 2 - Thrown when an incorrect PIN is entered.
public class InvalidPinException extends AccountException {
    public InvalidPinException(String message) {
        super(message);
    }
}
