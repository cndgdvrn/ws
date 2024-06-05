package com.boilerplate.ws.user;

import com.boilerplate.ws.error.ApiError;
import com.boilerplate.ws.shared.GenericMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user){
        userService.save(user);
        return ResponseEntity.status(200).body(new GenericMessage("User created successfully"));
    }




    //

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiError apiError = new ApiError();
        Map<String, String> validationErrors = new HashMap<>();
        apiError.setStatus(400);
        apiError.setPath("/api/v1/user");
        apiError.setMessage("Validation errors");

        System.out.println(ex.getBindingResult().getFieldErrors().size());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            System.out.println(fieldError.getField() + " : " + fieldError.getDefaultMessage());
        }

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }

        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);

    }

    @ExceptionHandler(NotUniqueEmailException.class)
    public ResponseEntity<ApiError> handleNotUniqueEmailException(NotUniqueEmailException ex){
        ApiError apiError = new ApiError();
        Map<String,String> validationErrors = new HashMap<>();
        apiError.setStatus(400);
        apiError.setPath("/api/v1/user");
        apiError.setMessage("Validation errors");

        validationErrors.put("email","Email already exist");
        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);

    }



}







