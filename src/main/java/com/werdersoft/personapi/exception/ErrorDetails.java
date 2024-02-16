package com.werdersoft.personapi.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ErrorDetails {
    private LocalDateTime timestamp;
    private Integer status;
    private List<String> errors;
    private String context;

    public ErrorDetails(Integer status, List<String> errors, String context) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.errors = errors;
        this.context = context;
    }
}
