package com.boilerplate.ws.configuration;

import com.boilerplate.ws.auth.exception.CustomJwtException;
import com.boilerplate.ws.auth.token.TokenService;
import com.boilerplate.ws.error.ApiError;
import com.boilerplate.ws.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("jwtTokenService")
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            try {
                User user = tokenService.verifyToken(authorizationHeader);
                if (user != null) {
                    if(!user.isActive()){
                        handleException(request,response, "User is not active");
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
                handleException(request,response, ex.getMessage());
                return;
            }
        }


        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletRequest request,HttpServletResponse response, String message) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        response.getWriter().write(
//                "{" +
//                        "\"timestamp\":\"" + new Date().getTime() + "\"," +
//                        "\"status\":401," +
//                        "\"message\":\"" + message + "\"," +
//                        "\"path\":\"" + request.getRequestURI() + "\"}"
//        );

        ApiError apiError = new ApiError();
        apiError.setMessage(message);
        apiError.setPath(request.getRequestURI());
        apiError.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setStatus(apiError.getStatus());
        response.setContentType("application/json");
        OutputStream os = response.getOutputStream();
        objectMapper.writeValue(os, apiError);
        os.flush();
    }

}
