package com.boilerplate.ws.auth.exception;
public class CustomJwtException extends RuntimeException{
    public CustomJwtException(String message) {
        super(message);
    }
}
