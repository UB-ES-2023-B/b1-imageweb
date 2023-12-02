package com.example.b1esimageweb.web.controller;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.example.b1esimageweb.web.dto.UserInfoDto;
//import com.example.b1esimageweb.web.Security.CurrentUserDetails;
import com.example.b1esimageweb.web.dto.PasswordResetDto;
import com.example.b1esimageweb.web.dto.UserUpdateDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        User user = service.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/getByUserName/{userName}")
    public ResponseEntity<UserInfoDto> getUserByUserName(@PathVariable("userName") String userName) {
        User user = service.getUserByUserName(userName);
        PhotoDto profilePhoto = service.getPhotoProfileByUser(user);
        UserInfoDto userInfoDto = new UserInfoDto(user.getUserId(), user.getUsername(), user.getUserEmail(), user.getPassword(), user.getDescription(), user.getGallery(), profilePhoto, user.isAccountNonExpired(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isEnabled(), user.getAuthorities());
        return new ResponseEntity<>(userInfoDto, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{username}")
    public ResponseEntity<Map<String, User>> updateUser(@RequestBody UserUpdateDto updated_user, @PathVariable("username") String username) {
        User userExisting = service.getUserByUserName(username);
        Map<String, User> response = new HashMap<>();
        if (userExisting != null) {
            if (!updated_user.getUsername().equalsIgnoreCase(userExisting.getUsername()) && service.userNameExists(updated_user.getUsername())) {
                response.put("Username already exists!", userExisting);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (!updated_user.getEmail().equalsIgnoreCase(userExisting.getUserEmail()) && service.emailExists(updated_user.getEmail())) {
                response.put("Email already exists!", userExisting);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            userExisting.setUsername(updated_user.getUsername());
            userExisting.setUserEmail(updated_user.getEmail());
            userExisting.setDescription(updated_user.getUserDescription());
            service.updateUser(userExisting);
            response.put("User details updated", userExisting);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new UserNotFoundException("User does not exists.");
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/uploadPhotoProfile")
    public ResponseEntity<String> uploadUserProfilePhoto(@RequestParam("profilePhoto") MultipartFile profilePhoto) {
        service.addProfilePicture(profilePhoto);
        return new ResponseEntity<>("Profile Picture successfully", HttpStatus.OK);
    }


    @DeleteMapping(path = "/deleteProfilePhoto")
    public ResponseEntity<String> deleteUserProfilePhoto() {
        service.deteleUserProficePicture();
        return new ResponseEntity<>("Profile Picture successfully deleted", HttpStatus.OK);
    }

    @GetMapping(path = "/viewPhotoProfile/{username}")
    public ResponseEntity<PhotoDto> viewUserProfilePhoto (@PathVariable("username") String username){
        User user = service.getUserByUserName(username);
        PhotoDto photo = service.getPhotoProfileByUser(user);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @PutMapping(path = "/resetPassword")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody PasswordResetDto passwordResetDto){
        Map<String, String> response = new HashMap<>();
        try {
            String msg = service.resetPassword(passwordResetDto, passwordEncoder);
            response.put("Message", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("Message", "Invalid current password");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}