package com.example.b1esimageweb.controller;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/user")
public class RequestController {

    private final UserService service;

    public RequestController(UserService service) {
        this.service = service;
    }
    @PostMapping(path="/addNew")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        User newUser = service.addNewUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /*@PostMapping(path="/getUserAccount")
    public ResponseEntity<User> getUserAccount(@RequestParam String userName, @RequestParam String userPassword) {
        User user = service.getUserAccount(userName, userPassword);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

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
