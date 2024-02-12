package com.example.NewsList.error;

public class ApiError extends Exception{

    public ApiError(String message) {
        super(message);
    }
}
