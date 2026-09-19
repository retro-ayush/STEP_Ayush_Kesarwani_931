package com.gdb.exceptions;

// Step 1 - AccountException is the base class for every banking exception.
//   It extends Exception, which makes it a checked exception.
public class AccountException extends Exception {
    public AccountException(String message) {
        super(message);
    }
}
