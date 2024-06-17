package com.boilerplate.ws.auth;

import com.boilerplate.ws.auth.dto.AuthResponse;
import com.boilerplate.ws.auth.dto.Credentials;
import com.boilerplate.ws.shared.OverriddenMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    OverriddenMessage overriddenMessage;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Credentials creds){
        AuthResponse authResponse = authService.authenticate(creds);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }



}
