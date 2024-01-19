package com.example.demo.controller;

import com.example.demo.error.ApiError;
import com.example.demo.error.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getFieldErrors().stream().map(e -> e.getDefaultMessage()).toList();
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), messages),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        List<String> messages = new ArrayList<>();
        messages.add(exception.getBody().getDetail());
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), messages),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ApiErrorResponse> handleApiError(ApiError exception) {
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), messages),
                HttpStatus.BAD_REQUEST);
    }

}
