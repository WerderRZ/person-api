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

    public ErrorDetails(LocalDateTime timestamp, Integer status, List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.errors = errors;
    }
}
