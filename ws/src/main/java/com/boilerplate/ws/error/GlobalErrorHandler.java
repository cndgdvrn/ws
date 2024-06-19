package com.boilerplate.ws.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException(DisabledException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());
        apiError.setStatus(401);
        return ResponseEntity.status(401).body(apiError);
    }




}
