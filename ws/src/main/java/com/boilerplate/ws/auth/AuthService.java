package com.boilerplate.ws.auth;

import com.boilerplate.ws.auth.dto.AuthResponse;
import com.boilerplate.ws.auth.dto.Credentials;
import com.boilerplate.ws.auth.exception.CustomAuthenticationException;
import com.boilerplate.ws.auth.token.Token;
import com.boilerplate.ws.auth.token.TokenService;
import com.boilerplate.ws.user.User;
import com.boilerplate.ws.user.UserService;
import com.boilerplate.ws.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
//    @Qualifier("basicAuthTokenService")
    @Qualifier("jwtTokenService")
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;



    public AuthResponse authenticate(Credentials creds) {
        User user = userService.getUserByEmail(creds.email());
        if(user == null || !passwordEncoder.matches(creds.password(), user.getPassword())){
            throw new CustomAuthenticationException();
        }
        Token token = tokenService.createToken(user, creds);
        UserDTO userDTO = new UserDTO(user);
        return new AuthResponse(userDTO,token);
    }
}
