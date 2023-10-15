package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.Service;
import com.example.b1esimageweb.web.dto.UserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(path="/login")
public class LoginController {

    private final Service service;

    @Autowired
    private PasswordEncoder passwordEncoder; //tendria que pasar sin encoder y encodear


    public LoginController(Service service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@ModelAttribute UserLoginDto userDto){
        
        if(!service.userNameExists(userDto.getUsername())){
            return new ResponseEntity<>("Username does not exists!", HttpStatus.BAD_REQUEST);
        }
        //password

        //User loginUser = service.getUser

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/home");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
