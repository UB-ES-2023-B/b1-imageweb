package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.Service;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(path="/register")
public class RegisterController {

    private final Service service;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public RegisterController(Service service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userDto){
        if(service.userNameExists(userDto.getUsername())){
            return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
        }
        if(service.emailExists(userDto.getEmail())){
            return new ResponseEntity<>("Email already exists!", HttpStatus.BAD_REQUEST);
        }
        User registerUser = new User(userDto.getUsername(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));
        service.addNewUser(registerUser);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>("Registration Successful",headers, HttpStatus.OK);
    }

}
