package com.boilerplate.ws.user;

import com.boilerplate.ws.error.ApiError;
import com.boilerplate.ws.shared.GenericMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user){
        System.out.println("----------------------------------------");
        System.out.println(
                LocaleContextHolder.getLocale().getLanguage()
        );
        System.out.println("----------------------------------------");
        userService.save(user);
        return ResponseEntity.status(200).body(new GenericMessage("User created successfully"));
    }





    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,WebRequest request) {
        ApiError apiError = new ApiError();
        Map<String, String> validationErrors = new HashMap<>();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setPath(request.getDescription(false));
        apiError.setMessage("Validation errors");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }
        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ApiError> handleNotUniqueException(NotUniqueException ex, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT.value());
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getDescription(false));
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }








}







