package com.boilerplate.ws.auth.token;

public class Token {
    String prefix;
    String token;

    public Token(String prefix, String token) {
        this.prefix = prefix;
        this.token = token;
    }

    public Token() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
