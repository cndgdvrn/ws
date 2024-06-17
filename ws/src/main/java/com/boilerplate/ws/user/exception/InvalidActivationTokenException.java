package com.boilerplate.ws.user.exception;

public class InvalidActivationTokenException extends RuntimeException {
    public InvalidActivationTokenException(String token) {
        super(token);
    }
}
