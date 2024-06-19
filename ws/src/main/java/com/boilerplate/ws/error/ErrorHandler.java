package com.boilerplate.ws.error;

import com.boilerplate.ws.auth.exception.CustomAuthenticationException;
import com.boilerplate.ws.shared.OverriddenMessage;
import com.boilerplate.ws.user.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @Autowired
    private OverriddenMessage overriddenMessage;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {

        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.validation.error");

        ApiError apiError = new ApiError();
        Map<String, String> validationErrors = new HashMap<>();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setPath(request.getDescription(false));
        apiError.setMessage(messageTemplate);
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }
        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(ActivationMailException.class)
    public ResponseEntity<ApiError> handleActivationMailException(ActivationMailException ex, WebRequest request) {
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.activation.mail.error");
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_GATEWAY.value());
        apiError.setPath(request.getDescription(false));
        apiError.setMessage(messageTemplate + " / " + ex.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ApiError> handleNotUniqueException(NotUniqueException ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT.value());
        apiError.setMessage("Validation error");
        apiError.setPath(request.getDescription(false));
        apiError.setValidationErrors(ex.getValidationErrors());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(InvalidActivationTokenException.class)
    public ResponseEntity<ApiError> handleInvalidActivationTokenException(InvalidActivationTokenException ex, WebRequest request) {
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.activation.user.error");
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setPath(request.getDescription(false) + "/?token=" + ex.getMessage());
        apiError.setMessage(messageTemplate);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<ApiError> handleUserNotFoundByIdException(UserNotFoundByIdException ex, WebRequest request) {
        String formattedId = String.format("%d", ex.getId());
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.user.not.found", formattedId);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setPath(request.getDescription(false));
        apiError.setMessage(messageTemplate);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(UserNotFoundByEmailException.class)
    public ResponseEntity<ApiError> handleUserNotFoundByEmailException(UserNotFoundByEmailException ex, WebRequest request) {
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.user.not.found.by.email", ex.getEmail());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setPath(request.getDescription(false));
        apiError.setMessage(messageTemplate);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }


    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ApiError> handleCustomAuthenticationException(CustomAuthenticationException ex, WebRequest request) {
        String message = overriddenMessage.getMessageFromLocale("boilerplate.auth.invalid.credentials");
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiError.setMessage(message);
        apiError.setPath(request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiError> handleAuthorizationException(AuthorizationException ex, WebRequest request) {
        String message = overriddenMessage.getMessageFromLocale("boilerplate.auth.unauthorized");
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.FORBIDDEN.value());
        apiError.setMessage(message);
        apiError.setPath(request.getDescription(false));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }


}
