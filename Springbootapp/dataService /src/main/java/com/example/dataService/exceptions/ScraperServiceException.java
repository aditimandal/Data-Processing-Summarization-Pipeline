package com.example.dataService.exceptions;

public class ScraperServiceException extends RuntimeException {
    public ScraperServiceException(String message) {
        super(message);
    }

    // Optional: Add a constructor with cause (for exception chaining)
    public ScraperServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}