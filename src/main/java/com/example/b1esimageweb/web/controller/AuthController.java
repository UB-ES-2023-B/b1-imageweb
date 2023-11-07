package com.example.b1esimageweb.web.controller;


import com.example.b1esimageweb.service.AuthService;
import com.example.b1esimageweb.web.dto.UserLoginDto;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRegistrationDto userDto){
        return ResponseEntity.ok(authService.register(userDto));
    }


    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody UserLoginDto userDto){
        return ResponseEntity.ok(authService.login(userDto));
    }
}
