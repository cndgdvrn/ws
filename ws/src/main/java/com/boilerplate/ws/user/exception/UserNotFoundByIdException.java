package com.boilerplate.ws.user.exception;

public class UserNotFoundByIdException extends RuntimeException {

    private final Long id ;

    public UserNotFoundByIdException(Long id) {
        super();
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
