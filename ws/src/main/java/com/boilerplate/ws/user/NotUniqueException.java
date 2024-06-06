package com.boilerplate.ws.user;

public class NotUniqueException extends RuntimeException{

    public NotUniqueException(String message) {
        super(message);
    }
}
