package com.antk7894.amatda.exception;

import com.antk7894.amatda.exception.custom.NoAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> notFoundHandle(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ExceptionHandler(NoAuthenticationException.class)
    public ResponseEntity<?> noAuthenticationHandle(NoAuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
