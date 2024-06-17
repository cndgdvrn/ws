package com.boilerplate.ws.user.dto;

import com.boilerplate.ws.user.validation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdate {

    @Size(min = 3, max = 50)
    @NotBlank(message = "{boilerplate.NotBlank.username}")
    @Unique(fieldName = "username")
    private String username;

    public UserUpdate() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }
}
