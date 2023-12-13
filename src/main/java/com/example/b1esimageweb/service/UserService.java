package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.InvalidPasswordException;
import com.example.b1esimageweb.Exceptions.PhotoNotFoundException;
import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import com.example.b1esimageweb.web.dto.PasswordResetDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GalleryRepository galleryRepository;
    private final PhotoRepository photoRepository;

    private CloudStorageAccount account;
    private CloudBlobClient serviceClient;
    private CloudBlobContainer container;

    public UserService(UserRepository userRepository, GalleryRepository galleryRepository, PhotoRepository photoRepository, @Value("${azure.storage.conection.string}") String storageConnectionAzure,  @Value("${azure.storage.container.name}") String nameContainer) {
        this.userRepository = userRepository;
        this.galleryRepository = galleryRepository;
        this.photoRepository = photoRepository;

        try {
            account = CloudStorageAccount.parse(storageConnectionAzure);
            serviceClient = account.createCloudBlobClient();
            container = serviceClient.getContainerReference(nameContainer);
        } catch (InvalidKeyException | URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    protected User getCurrentUserFromConext(){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null;
        if(obj instanceof User){
            currentUser = (User) obj;
        }
        return currentUser;
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
        User currentUser = getCurrentUserFromConext();
        if(currentUser != null) {
            Photo profilePhoto = new Photo();

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
                CloudBlockBlob blockBlob;
                Photo photoToDelete =  photoRepository.findById(oldPhoto.getPhotoId()).orElseThrow(()-> new PhotoNotFoundException("Photo with id " + oldPhoto.getPhotoId() + "not found"));
                try {
                    blockBlob = container.getBlockBlobReference(photoToDelete.getPhotoId().toString());
                    blockBlob.deleteIfExists();
                } catch (URISyntaxException | StorageException e) {
                    e.printStackTrace();
                    return null;
                }

            }
            CloudBlob blob;
            try {
                blob = container.getBlockBlobReference(profilePhoto.getPhotoId().toString());
                byte[] decodedBytes = photo.getBytes();
                blob.uploadFromByteArray(decodedBytes, 0, decodedBytes.length); 
            } catch (URISyntaxException | StorageException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return photoRepository.save(profilePhoto);
        }else{
            throw new UserNotFoundException("User does not exists");
        }
    }


    public void deteleUserProficePicture(){
        User currentUser = getCurrentUserFromConext();
        if(currentUser != null) {
            Photo profilePhoto = currentUser.getProfilePicture();
            currentUser.setProfilePicture(null);
            userRepository.save(currentUser);
            photoRepository.delete(profilePhoto);
            CloudBlockBlob blockBlob;
             try {
                blockBlob = container.getBlockBlobReference(profilePhoto.getPhotoId().toString());
                blockBlob.deleteIfExists();
            } catch (URISyntaxException | StorageException e) {
                e.printStackTrace();
            }
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

    public PhotoDto getPhotoProfileByUser(User user){
        Photo photo = userRepository.getPhotoProfileByUserId(user.getUserId());
        if(photo!=null){
            CloudBlob blob;
            try {
                blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blob.download(outputStream);
                byte[] photoContent = outputStream.toByteArray();
                return new PhotoDto(photoContent, photo.getPhotoId(), photo.getGallery(), photo.getPhotoName(), photo.getAlbums(), photo.getPhotoExtension(), photo.getPhotoDescription());
            } catch (URISyntaxException | StorageException e) {
                e.printStackTrace();
                
            }
        }
        return null;
    }
    
    public Gallery getGalleryByUser(User user){
        return userRepository.getGalleryByUserId(user.getUserId());
    }

    public User getUserByGallery(Gallery gallery){
        int userId = userRepository.getUserByGallery(gallery);
        return getUserById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).get();
    }

    public String resetPassword (PasswordResetDto passwordResetDto, PasswordEncoder passwordEncoder){
        User currentUser = getCurrentUserFromConext();
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

    public Map<String, Object>  followUser(String userToFollowUsername) throws Exception {
        User currentUser = getCurrentUserFromConext();
        User userToFollow = userRepository.findByUsername(userToFollowUsername)
                .orElseThrow(() -> new Exception("User not found with name: " + userToFollowUsername));

        currentUser.followUser(userToFollow);
        userRepository.save(currentUser);
        userRepository.save(userToFollow);

        return getFollowOrFollowingAndPhotos(currentUser, false);
    }

    public Map<String, Object> unfollowUser(String userToUnfollowUsername) throws Exception {
        User currentUser = getCurrentUserFromConext();
        User userToUnfollow = userRepository.findByUsername(userToUnfollowUsername)
                .orElseThrow(() -> new Exception("User not found with name: " + userToUnfollowUsername));

        currentUser.unfollowUser(userToUnfollow);
        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);

        return getFollowOrFollowingAndPhotos(currentUser, false);
    }
    public Map<String, Object> getFollowerOrFollowed(String username, boolean followersOrFollowing) throws Exception {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new Exception("User not found with username: " + username));
        return getFollowOrFollowingAndPhotos(user, followersOrFollowing);
    }

    //followersOrFollowing = true -> returns Followers List
    //followersOrFollowing = false -> return Following list
    protected Map<String, Object> getFollowOrFollowingAndPhotos(User user, boolean followersOrFollowing) {
        Set<User> follow= followersOrFollowing ? user.getFollowers() : user.getFollowing();
        List<Map<String, Object>> listWithPhotos = follow.stream()
                .map(follower -> {
                    int photoCount = photoRepository.countByGallery(follower.getGallery());
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("username", follower.getUsername());
                    userMap.put("numPhotosPublicas", photoCount);
                    return userMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put(followersOrFollowing ? "followers" : "following", listWithPhotos);
        return response;
    }
}
