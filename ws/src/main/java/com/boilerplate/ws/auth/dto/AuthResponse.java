package com.boilerplate.ws.auth.dto;

import com.boilerplate.ws.auth.token.Token;
import com.boilerplate.ws.user.dto.UserDTO;

public class AuthResponse {

    private UserDTO user;

    private Token token;

    public AuthResponse() {}

    public AuthResponse(UserDTO user, Token token) {
        this.user = user;
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public AuthResponseForCookie toAuthResponseForCookie(AuthResponse authResponse) {
        return new AuthResponseForCookie(authResponse.getUser());
    }

    private static record AuthResponseForCookie(UserDTO user) {}
}

