package com.example.b1esimageweb.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("HELLO MAIN PAGE", HttpStatus.FOUND);
    }
}
