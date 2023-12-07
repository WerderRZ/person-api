package com.werdersoft.personapi.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ErrorDetails {
    private Date timestamp;
    private Integer status;
    private List<String> errors;

    public ErrorDetails(Date timestamp, Integer status, List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.errors = errors;
    }
}
