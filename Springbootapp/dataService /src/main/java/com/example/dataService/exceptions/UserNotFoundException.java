package com.example.dataService.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    // Optional: Add a constructor with cause (for exception chaining)
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}