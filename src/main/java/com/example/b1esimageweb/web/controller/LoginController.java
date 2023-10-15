package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.Service;
import com.example.b1esimageweb.web.dto.UserLoginDto;
import com.example.b1esimageweb.web.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/login")
public class LoginController {

    private final Service service;

    @Autowired
    private PasswordEncoder passwordEncoder; //tendria que pasar sin encoder y encodear

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public LoginController(Service service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody UserLoginDto userDto){
        Map<String, String> response = new HashMap<>();
        if(!service.userNameExists(userDto.getUsername())){
            response.put("message", "Username does not exists!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User loginUser = service.getUserByUserName(userDto.getUsername());
        String userPassword = loginUser.getUserPassword();
        if(!passwordEncoder.matches(userDto.getPassword(),userPassword)){
            response.put("message", "Incorrect Password!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String token = jwtTokenProvider.createToken(loginUser);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        response.put("token", token);
        return new ResponseEntity<>(response,headers, HttpStatus.OK);
    }

}
