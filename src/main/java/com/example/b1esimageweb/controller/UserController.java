package com.example.b1esimageweb.controller;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String password) {
        User user = new User();
        user.setUserName(name);
        user.setUserPassword(password);
        userRepository.save(user);
        return "Saved User";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path="/byid")
    public @ResponseBody User getUserById(@RequestParam Integer id) {
        return userRepository.findById(id).get();
    }



}
