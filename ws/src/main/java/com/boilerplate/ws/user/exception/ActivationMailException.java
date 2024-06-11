package com.boilerplate.ws.user.exception;


import java.util.Map;

public class ActivationMailException extends RuntimeException {

    private Map<String, String> validationErrors;

    public ActivationMailException() {
        super();
    }

    public ActivationMailException(String message) {
        super(message);
    }

    public ActivationMailException(Map<String, String> validationErrors) {
        super();
        this.validationErrors = validationErrors;
    }

    public ActivationMailException(String message, Map<String, String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }


    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

}
