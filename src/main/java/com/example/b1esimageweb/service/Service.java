package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class Service {

    private UserRepository repository;
    @Autowired
    public Service(UserRepository repository) {
        this.repository = repository;
    }

    public User addNewUser(User user) {
        return repository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(int id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User updateUser(User user) {
        return repository.save(user);
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }

    public User getUserByUserEmail(String email){
        return repository.getUserByUserEmail(email);
    }

    public User getUserByUserName(String userName){
        return repository.getUserByUserName(userName);
    }
    public boolean userNameExists(String userName){
        return repository.existsUserByUserName(userName);
    }

    public boolean emailExists(String email){
        return repository.existsUserByUserEmail(email);
    }
}
