package com.boilerplate.ws.user.dto;

import com.boilerplate.ws.user.User;
import com.boilerplate.ws.user.validation.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserCreate {

    @Size(min = 3, max = 50)
    @NotBlank(message = "{boilerplate.NotBlank.username}")
    @Unique(fieldName = "username")
    private String username;

    @Email
    @NotBlank(message = "{boilerplate.NotBlank.email}")
    @Unique(fieldName = "email")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{boilerplate.pattern.password}")
    @Size(min = 8, max = 255, message = "{boilerplate.size.password}")
    private String password;

    public UserCreate() {
    }

    public User toUser(){
        User user = new User();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
