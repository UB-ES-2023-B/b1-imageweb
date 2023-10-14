package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User addNewUser(User user){
        return repository.save(user);
    }

    public Iterable<User> getAllUsers(){
        return repository.findAll();
    }

    /*public User getUserAccount(String userName, String userPassword){
        return repository.findByUsernameAndPassword(userName, userPassword);
    }*/

    public User getUserById(int id){
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User updateUser(User user){
        return repository.save(user);
    }

    public void deleteUser(int id){
        repository.deleteById(id);
    }

}
