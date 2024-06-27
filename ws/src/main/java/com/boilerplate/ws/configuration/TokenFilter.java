package com.boilerplate.ws.configuration;

import com.boilerplate.ws.auth.token.TokenService;
import com.boilerplate.ws.auth.token.blacklisted.BlacklistedTokenService;
import com.boilerplate.ws.error.ApiError;
import com.boilerplate.ws.shared.GenericMessage;
import com.boilerplate.ws.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;


@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("jwtTokenService")
    private TokenService tokenService;

    @Autowired
    private BlacklistedTokenService blacklistedTokenService;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String authorizationHeader = request.getHeader("Authorization");
        String extractedToken = tokenService.extractToken(authorizationHeader);


        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ws-token")) {
                    extractedToken = cookie.getValue();
                    authorizationHeader = "DummyPrefix " + extractedToken;
                    break;
                }
            }
        }

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/auth/logout")) {
            if (extractedToken != null && blacklistedTokenService.isBlacklisted(extractedToken)) {
                handleException(request, response, "Token is blacklisted, logged out successfully", true);
                return;
            }
        }

        if (extractedToken != null && blacklistedTokenService.isBlacklisted(extractedToken)) {
            handleException(request, response, "Token is blacklisted", false);
            return;
        }
        if (authorizationHeader != null) {
            try {
                User user = tokenService.verifyToken(authorizationHeader);
                if (user != null) {
                    if (!user.isActive()) {
                        handleException(request, response, "User is not active", false);
                        return;
                    }
                    CurrentUser currentUser = new CurrentUser(user);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            currentUser,
                            null,
                            currentUser.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException ex) {
                handleException(request, response, ex.getMessage(), false);
                return;
            }
        }


        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, String message, boolean success) throws IOException {

        OutputStream os = response.getOutputStream();
        response.setContentType("application/json");
        if (!success) {
            ApiError apiError = new ApiError();
            apiError.setMessage(message);
            apiError.setPath(request.getRequestURI());
            apiError.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setStatus(apiError.getStatus());
            objectMapper.writeValue(os, apiError);
            os.flush();
        } else {
            GenericMessage genericMessage = new GenericMessage(message);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(os, genericMessage);
            os.flush();
        }
    }
}