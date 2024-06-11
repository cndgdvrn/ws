package com.boilerplate.ws.user;

import com.boilerplate.ws.error.ApiError;
import com.boilerplate.ws.shared.GenericMessage;
import com.boilerplate.ws.shared.OverriddenMessage;
import com.boilerplate.ws.user.dto.UserCreate;
import com.boilerplate.ws.user.exception.ActivationMailException;
import com.boilerplate.ws.user.exception.InvalidTokenException;
import com.boilerplate.ws.user.exception.NotUniqueException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OverriddenMessage overriddenMessage;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreate user) {
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.user.created.successfully");
        userService.save(user.toUser());
        return ResponseEntity.status(200).body(new GenericMessage(messageTemplate));
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestParam String token) {
        userService.activateUser(token);
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.activation.user.successfully");
        return ResponseEntity.status(200).body(new GenericMessage(messageTemplate));
    }

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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException ex,WebRequest request){
        String messageTemplate = overriddenMessage.getMessageFromLocale("boilerplate.activation.user.error");
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setPath(request.getDescription(false) + "/?token=" + ex.getMessage());
        apiError.setMessage(messageTemplate);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

}







