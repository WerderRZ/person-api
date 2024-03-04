package com.werdersoft.personapi.utils;

import com.werdersoft.personapi.exception.ErrorDetails;

import java.util.ArrayList;
import java.util.List;

public class ErrorDetailsFactory {

    public static ErrorDetails newErrorDetails400Check(List<String> errors) {
        return getErrorDetailsWithOneError(400, errors);

    }

    public static ErrorDetails newErrorDetails404IncorrectURI(String uri) {
        List<String> errors = new ArrayList<>();
        errors.add("Incorrect request URI:" + uri);
        return getErrorDetailsWithOneError(404, errors);
    }

    private static ErrorDetails getErrorDetailsWithOneError(Integer status, List<String> errors) {
        return ErrorDetails.builder()
                .status(status)
                .errors(errors)
                .build();
    }

}
