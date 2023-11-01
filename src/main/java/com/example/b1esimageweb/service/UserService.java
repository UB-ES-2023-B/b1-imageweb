package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GalleryRepository galleryRepository;
    private final PhotoRepository photoRepository;

    public UserService(UserRepository userRepository, GalleryRepository galleryRepository, PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.galleryRepository = galleryRepository;
        this.photoRepository = photoRepository;
    }

    // Now we add(register) new user in the AuthService class
    // this method is not used anymore
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
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null;
        if(obj instanceof User){
            currentUser = (User) obj;
        }
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
        return userRepository.findUserByEmail(email);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).get();
    }

    public User getUserByUserName(String username){
        return userRepository.findByUsername(username).get();
    }
    public boolean emailExists(String email){
        return userRepository.existsUserByEmail(email);
    }

    public boolean userNameExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

}
