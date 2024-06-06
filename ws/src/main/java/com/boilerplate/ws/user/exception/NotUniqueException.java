package com.boilerplate.ws.user.exception;

import java.util.Map;

public class NotUniqueException extends RuntimeException{

    private Map<String,String> validationErrors;

    public NotUniqueException(String message, Map<String,String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}
