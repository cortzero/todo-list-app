package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.apiresponses.ErrorResponse;
import com.cortzero.todoapi.exceptions.ResourceNotFoundException;
import com.cortzero.todoapi.exceptions.UserEmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlerUserEmailAlreadyExistsException(UserEmailAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
