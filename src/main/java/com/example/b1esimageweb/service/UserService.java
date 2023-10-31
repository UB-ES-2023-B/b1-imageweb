package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.Role;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.web.Jwt.JwtTokenProvider;
import com.example.b1esimageweb.web.Security.CurrentUserDetails;
import com.example.b1esimageweb.web.controller.AuthResponse;
import com.example.b1esimageweb.web.dto.UserLoginDto;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private GalleryRepository galleryRepository;
    private PhotoRepository photoRepository;

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
        Optional<User> currentUser = this.getUserByUsername(currentUserName);
        User curr = currentUser.get();
        if(currentUser.isPresent()) {
            Photo profilePhoto = new Photo();
            try {
                profilePhoto.setData(photo.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            profilePhoto.setPhotoName(photo.getOriginalFilename());
            curr.setProfilePicture(profilePhoto);
            userRepository.save(curr);
            return photoRepository.save(profilePhoto);
        }else{
            throw new UserNotFoundException("User does not exists");
        }
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public User getUserByUserEmail(String email){
        return userRepository.findUserByUserEmail(email);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User getUserByUserName(String username){
        return userRepository.getUserByUserName(username);
    }
    public boolean emailExists(String email){
        return userRepository.existsUserByUserEmail(email);
    }

    public boolean userNameExists(String userName) {
        return userRepository.existsUserByUserName(userName);
    }

}
