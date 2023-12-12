package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.AuthService;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.dto.UserLoginDto;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
import com.example.b1esimageweb.web.responses.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRegistrationDto userDto){
        try{
            User userExisting = userService.getUserByUserName(userDto.getUsername());
            if(userExisting != null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AuthResponse("Username Already Exists."));

            }

        }catch (NoSuchElementException e){
            if (userService.emailExists(userDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AuthResponse("Email already Exists."));
            }
            return ResponseEntity.ok(authService.register(userDto));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse("User Already Exists."));
    }


    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody UserLoginDto userDto){
        return ResponseEntity.ok(authService.login(userDto));
    }
}
