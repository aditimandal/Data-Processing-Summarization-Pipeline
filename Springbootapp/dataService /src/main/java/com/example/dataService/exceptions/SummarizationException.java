package com.example.dataService.exceptions;

public class SummarizationException extends RuntimeException {
    public SummarizationException(String message) {
        super(message);
    }

    // Optional: Constructor with cause (for exception chaining)
    public SummarizationException(String message, Throwable cause) {
        super(message, cause);
    }
}