package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class UserService {

    private UserRepository userRepository;
    private GalleryRepository galleryRepository;

    @Autowired
    public UserService(UserRepository repository, GalleryRepository galleryRepository) {
        this.userRepository = repository;
        this.galleryRepository = galleryRepository;
    }

    public User addNewUser(User user) {
        Gallery gallery = new Gallery();
        gallery.setGalleryName("My first Gallery");
        galleryRepository.save(gallery);
        user.setGallery(gallery);
        return userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public User getUserByUserEmail(String email){
        return userRepository.getUserByUserEmail(email);
    }

    public User getUserByUserName(String userName){
        return userRepository.getUserByUserName(userName);
    }

    public boolean userNameExists(String userName){
        return userRepository.existsUserByUserName(userName);
    }

    public boolean emailExists(String email){
        return userRepository.existsUserByUserEmail(email);
    }
}
