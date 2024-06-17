package com.boilerplate.ws.auth.token;

import com.boilerplate.ws.auth.dto.Credentials;
import com.boilerplate.ws.user.User;
import com.boilerplate.ws.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class BasicAuthTokenService implements TokenService {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Token createToken(User user, Credentials creds) {
        String emailPassword = user.getEmail() + ":" + user.getPassword();
        byte[] emailPasswordBytes = emailPassword.getBytes();
        String encodedToken = Base64.getEncoder().encodeToString(emailPasswordBytes);
        return new Token("Basic", encodedToken);
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        if (authorizationHeader == null) return null;
        byte[] decodedBytes = Base64.getDecoder().decode(authorizationHeader.split(" ")[1]);
        String decoded = new String(decodedBytes);
        String[] credentials = decoded.split(":");
        String email = credentials[0];
        String password = credentials[1];
        User user = userService.getUserByEmail(email);
        if (user == null || !(passwordEncoder.matches(password, user.getPassword()))) return null;
        return user;
    }


}
