package com.boilerplate.ws.auth;

import com.boilerplate.ws.auth.dto.AuthResponse;
import com.boilerplate.ws.auth.dto.Credentials;
import com.boilerplate.ws.auth.token.blacklisted.BlacklistedToken;
import com.boilerplate.ws.auth.token.blacklisted.BlacklistedTokenService;
import com.boilerplate.ws.shared.GenericMessage;
import com.boilerplate.ws.shared.OverriddenMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private BlacklistedTokenService blacklistedTokenService;

    @Autowired
    private OverriddenMessage overriddenMessage;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Credentials creds) {
        AuthResponse authResponse = authService.authenticate(creds);
        ResponseCookie cookie = ResponseCookie.from("ws-token", authResponse.getToken().getToken())
                .httpOnly(true)
                .maxAge(60 * 60 * 12) // 12 hours
                .path("/")
                .sameSite("Strict")
                .secure(true)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Set-Cookie", cookie.toString())
                .body(authResponse.toAuthResponseForCookie(authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        ResponseCookie.from("ws-token", "")
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .sameSite("Strict")
                .secure(true)
                .build();

        if (authorizationHeader == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new GenericMessage("Logged out successfully"));
        }
        String token = authorizationHeader.split(" ")[1];
        blacklistedTokenService.save(new BlacklistedToken(token));
        return ResponseEntity.status(HttpStatus.OK).body(new GenericMessage("Logged out successfully"));
    }
}
