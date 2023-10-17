package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.Service;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/user")
public class UserController {

    private final Service service;

    public UserController(Service service) {
        this.service = service;
    }
    
    @PostMapping(path="/addNew")
    public ResponseEntity<User> addNewUser(@RequestBody UserRegistrationDto user) {
        User newUser = new User(user.getUsername(), user.getEmail(), user.getPassword());
        service.addNewUser(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<Iterable<User>> getAllUsers(){
        Iterable<User> users = service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path="/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        User user = service.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path="/getByUserName/{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable("userName") String userName) {
        User user = service.getUserByUserName(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path="/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = service.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
