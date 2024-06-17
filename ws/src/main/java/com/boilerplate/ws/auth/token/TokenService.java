package com.boilerplate.ws.auth.token;

import com.boilerplate.ws.auth.dto.Credentials;
import com.boilerplate.ws.user.User;

public interface TokenService {

    public Token createToken(User user, Credentials creds);

    public User verifyToken(String authorizationHeader);
}
