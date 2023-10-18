package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.Service;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/user")
public class UserController {

    private final Service service;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public ResponseEntity<User> updateUser(@RequestBody UserRegistrationDto updated_user) {
        System.out.println(updated_user.getUsername());
        User userExisting = service.getUserByUserName(updated_user.getUsername());
        if (userExisting != null){
            userExisting.setUserName(updated_user.getUsername());
            userExisting.setUserEmail(updated_user.getEmail());
            userExisting.setUserPassword(passwordEncoder.encode(updated_user.getPassword()));
            service.updateUser(userExisting);
            return new ResponseEntity<>(userExisting, HttpStatus.OK);
        }else{
            throw new UserNotFoundException("User does not exists.");
        }
    }

    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
