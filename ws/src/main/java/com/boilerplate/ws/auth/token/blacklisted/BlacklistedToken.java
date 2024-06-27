package com.boilerplate.ws.auth.token.blacklisted;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BlacklistedToken {

    @Id
    private String token;

    public BlacklistedToken() {
    }

    public BlacklistedToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
