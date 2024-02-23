package com.werdersoft.personapi.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleValidationException(BindException ex, HttpServletRequest request) {
        var uri = request.getRequestURI();
        var body = request.getQueryString();
        List<String> errorMessages = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.toList());
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 400, errorMessages);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {
        List<String> errorMessages = List.of("Incorrect request URI:" + request.getRequestURI());
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getRawStatusCode(), errorMessages);
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        List<String> errorMessages = List.of("Сouldn't read the message:" + request.getRequestURI());
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 400, errorMessages);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception ex) {
        Class cl = ex.getClass();
        List<String> errorMessages = new ArrayList<>();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 400, errorMessages);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
