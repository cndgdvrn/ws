package com.boilerplate.ws.user.dto;

import com.boilerplate.ws.user.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;


    private String image;

    public UserDTO() {
    }

    public UserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        setImage(user.getImage());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (image == null) {
            this.image = "default.png";
        } else {
            this.image = image;
        }
    }
}
