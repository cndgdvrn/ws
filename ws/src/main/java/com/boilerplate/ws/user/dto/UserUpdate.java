package com.boilerplate.ws.user.dto;

import com.boilerplate.ws.user.validation.FileType;
import com.boilerplate.ws.user.validation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdate {

    @Size(min = 3, max = 50)
    @NotBlank(message = "{boilerplate.NotBlank.username}")
    @Unique(fieldName = "username")
    private String username;

    @FileType(allowedTypes = {"image/jpeg", "image/png", "image/jpg"})
    private String image;


    public UserUpdate() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
