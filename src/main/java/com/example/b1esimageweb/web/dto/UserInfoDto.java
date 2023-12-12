package com.example.b1esimageweb.web.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.example.b1esimageweb.model.Gallery;


public class UserInfoDto {
    
    private Integer userId;
    private String username, email, password, description;
    private Gallery gallery;
    private PhotoDto profilePicture;
    private boolean accountNonExpired, accountNonLocked, credentialsNonExpired, enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserInfoDto(Integer userId, String username, String email, String password, String description,
            Gallery gallery, PhotoDto profilePicture, boolean accountNonExpired, boolean accountNonLocked,
            boolean credentialsNonExpired, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = description;
        this.gallery = gallery;
        this.profilePicture = profilePicture;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public PhotoDto getProfilePicture() {
        return profilePicture;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
