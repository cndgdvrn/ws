package com.boilerplate.ws.user.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super(token);
    }
}
