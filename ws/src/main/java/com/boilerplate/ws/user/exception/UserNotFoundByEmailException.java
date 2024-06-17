package com.boilerplate.ws.user.exception;

public class UserNotFoundByEmailException extends RuntimeException {
    private String email;
    public UserNotFoundByEmailException(String email) {
        super();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
