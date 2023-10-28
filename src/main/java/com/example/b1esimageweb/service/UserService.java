package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.web.Security.CurrentUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@org.springframework.stereotype.Service
public class UserService {

    private UserRepository userRepository;
    private GalleryRepository galleryRepository;
    private PhotoRepository photoRepository;

    @Autowired
    public UserService(UserRepository repository, GalleryRepository galleryRepository, PhotoRepository photoRepository) {
        this.userRepository = repository;
        this.galleryRepository = galleryRepository;
        this.photoRepository = photoRepository;
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

    public Photo addProfilePicture (MultipartFile photo){
        CurrentUserDetails details = new CurrentUserDetails();
        String currentUserName = details.getCurrentLoggedInUser();
        User currentUser = this.getUserByUserName(currentUserName);
        if(currentUser != null) {
            Photo profilePhoto = new Photo();
            try {
                profilePhoto.setData(photo.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            profilePhoto.setPhotoName(photo.getOriginalFilename());
            currentUser.setProfilePicture(profilePhoto);
            userRepository.save(currentUser);
            return photoRepository.save(profilePhoto);
        }else{
            throw new UserNotFoundException("User does not exists");
        }
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
