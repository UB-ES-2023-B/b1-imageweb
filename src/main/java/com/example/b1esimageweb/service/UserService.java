package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.InvalidPasswordException;
import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.web.dto.PasswordResetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

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
            String fileName = photo.getOriginalFilename();
            int lastDotIndex = fileName.lastIndexOf(".");
            String extension = fileName.substring(lastDotIndex + 1);
            profilePhoto.setPhotoName(fileName);
            profilePhoto.setPhotoExtension(extension);

            Photo oldPhoto = currentUser.getProfilePicture();
            if(oldPhoto != null){
                int oldPhotoId  = oldPhoto.getPhotoId();
                currentUser.setProfilePicture(null);
                profilePhoto.setPhotoId(oldPhotoId);
            }
            currentUser.setProfilePicture(profilePhoto);
            userRepository.save(currentUser);

            if(oldPhoto != null){
                photoRepository.delete(oldPhoto);
            }
            return photoRepository.save(profilePhoto);
        }else{
            throw new UserNotFoundException("User does not exists");
        }
    }


    public void deteleUserProficePicture(){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null;
        if(obj instanceof User){
            currentUser = (User) obj;
        }
        if(currentUser != null) {
            Photo profilePhoto = currentUser.getProfilePicture();
            currentUser.setProfilePicture(null);
            userRepository.save(currentUser);
            photoRepository.delete(profilePhoto);
        }else{
            throw new UserNotFoundException("User does not exists");
        }
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public User getUserByUserName(String username){
        return userRepository.findByUsername(username).get();
    }

    public User getUserByUserEmail(String email){
        return userRepository.findUserByEmail(email);
    }
    public boolean emailExists(String email){
        return userRepository.existsUserByEmail(email);
    }

    public boolean userNameExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public Photo getPhotoProfileByUser(User user){
        return userRepository.getPhotoProfileByUserId(user.getUserId());
    }
    
    public Gallery getGalleryByUser(User user){
        return userRepository.getGalleryByUserId(user.getUserId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).get();
    }

    public String resetPassword (PasswordResetDto passwordResetDto, PasswordEncoder passwordEncoder){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null;
        if(obj instanceof User){
            currentUser = (User) obj;
        }
        if(currentUser != null) {
           if(passwordEncoder.matches(passwordResetDto.getCurrentPassword(), currentUser.getPassword())){
               currentUser.setUserPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
               userRepository.save(currentUser);
           }else{
               throw new InvalidPasswordException("Invalid current password");
           }
        }else{
            throw new UserNotFoundException("User does not exists");
        }
        return "Your password was changed successfully";
    }
}
