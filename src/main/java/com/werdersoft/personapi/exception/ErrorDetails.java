package com.werdersoft.personapi.exception;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ErrorDetails {
    private LocalDateTime timestamp;
    private Integer status;
    private List<String> errors;

    @Builder
    public ErrorDetails(LocalDateTime timestamp, Integer status, List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.errors = errors;
    }
}
