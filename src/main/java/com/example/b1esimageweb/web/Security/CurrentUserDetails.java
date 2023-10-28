package com.example.b1esimageweb.web.Security;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.UserService;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserDetails {
    public CurrentUserDetails(){}

    public String getCurrentLoggedInUser(){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj != null){
            return (String)obj;
        }
        return null;
    }
}
