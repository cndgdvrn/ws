package com.boilerplate.ws.error;

import com.boilerplate.ws.auth.exception.CustomJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException(DisabledException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage() + "DisabledException");
        apiError.setPath(request.getRequestURI());
        apiError.setStatus(401);
        return ResponseEntity.status(401).body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage() + "AccessDeniedException");
        apiError.setPath(request.getRequestURI());
        apiError.setStatus(403);
        return ResponseEntity.status(403).body(apiError);
    }


}
