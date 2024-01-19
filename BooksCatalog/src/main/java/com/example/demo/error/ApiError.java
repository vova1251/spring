package com.example.demo.error;

public class ApiError extends Exception {
    public ApiError(String message) {
        super(message);
    }
}
