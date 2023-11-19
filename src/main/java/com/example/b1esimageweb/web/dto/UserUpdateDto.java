package com.example.b1esimageweb.web.dto;

public class UserUpdateDto {
    private String username;
    private String email;
    private String description;

    public UserUpdateDto(String username, String email, String description) {
        this.username = username;
        this.email = email;
        this.description = description;
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

    public String getUserDescription() {
        return description;
    }

    public void setUserDescription(String description) {
        this.description = description;
    }
}

