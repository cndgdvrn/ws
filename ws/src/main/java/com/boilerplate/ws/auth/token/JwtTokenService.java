package com.boilerplate.ws.auth.token;

import com.boilerplate.ws.auth.dto.Credentials;
import com.boilerplate.ws.auth.exception.CustomJwtException;
import com.boilerplate.ws.user.User;
import com.boilerplate.ws.user.exception.AuthorizationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtTokenService implements TokenService {

    SecretKey secretKey = Keys.hmacShaKeyFor("/^8}KLFF]qkD0yyx=}8izr:;,VLXDu:0qUQss!Ik:Ojq[xrkud9}x6SUV0%'k@I".getBytes());

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Token createToken(User user, Credentials creds) {

        TokenSubject tokenSubject = new TokenSubject(user.getId(), user.isActive());
        try {
            String tokenSubjectAsString = objectMapper.writeValueAsString(tokenSubject);
            String token = Jwts.builder().setSubject(tokenSubjectAsString).signWith(secretKey).compact();
            return new Token("Bearer", token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User verifyToken(String authorizationHeader) {

        try {
            if (authorizationHeader == null) return null;
            try {
                String token = authorizationHeader.split(" ")[1];
                Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
                TokenSubject tokenSubject = objectMapper.readValue(claims.getBody().getSubject(), TokenSubject.class);
                User user = new User();
                user.setId(tokenSubject.getId());
                user.setActive(tokenSubject.isActive());
                return user;
            } catch (JsonProcessingException ex) {
                throw new JwtException(ex.getMessage());
            }
        }catch (JwtException ex){
            throw new JwtException(ex.getMessage());
        }
    }

    static class TokenSubject{
        private Long id;
        private boolean active;

        public TokenSubject() {
        }

        public TokenSubject(Long id, boolean active) {
            this.id = id;
            this.active = active;
        }

        public Long getId() {
            return id;
        }

        public boolean isActive() {
            return active;
        }
    }

}





