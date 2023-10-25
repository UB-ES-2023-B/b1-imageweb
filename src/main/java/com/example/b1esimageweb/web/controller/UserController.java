package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="/user")
public class UserController {

    private final UserService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService service) {
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

    @PutMapping(path="/update/{username}")
    public ResponseEntity<Map<String,User>> updateUser(@RequestBody UserRegistrationDto updated_user, @PathVariable("username") String username ) {
        User userExisting = service.getUserByUserName(username);
        Map<String, User> response = new HashMap<>();
        if (userExisting != null){
            if(!updated_user.getUsername().equalsIgnoreCase(userExisting.getUserName())  && service.userNameExists(updated_user.getUsername())){
                response.put("Username already exists!",userExisting);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if(!updated_user.getEmail().equalsIgnoreCase(userExisting.getUserEmail()) && service.emailExists(updated_user.getEmail())) {
                response.put("Email already exists!",userExisting);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            userExisting.setUserName(updated_user.getUsername());
            userExisting.setUserEmail(updated_user.getEmail());
            service.updateUser(userExisting);
            response.put("User details updated", userExisting);
            return new ResponseEntity<>(response, HttpStatus.OK);
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
