package com.werdersoft.personapi.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleBadRequestAPIException(BindException be) {
        List<String> errorMessages = be.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return getResponseEntityOfError(HttpStatus.BAD_REQUEST, errorMessages, "Bad request in service");
    }

    @ExceptionHandler(WebClientResponseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDetails> handleWebClientNotFoundException(WebClientResponseException wcre) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(wcre.getMessage());
        return getResponseEntityOfError(HttpStatus.NOT_FOUND, errorMessages,
                "Get data from web client exception");
    }

    private ResponseEntity<ErrorDetails> getResponseEntityOfError(HttpStatus status,
                                                                  List<String> errorMessages,
                                                                  String context) {
        ErrorDetails errorDetails = new ErrorDetails(status.value(), errorMessages, context);
        return new ResponseEntity<>(errorDetails, status);
    }
}
