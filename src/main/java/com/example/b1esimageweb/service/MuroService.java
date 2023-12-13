package com.example.b1esimageweb.service;

import java.util.ArrayList;
import java.util.List;

import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.web.dto.MuroDto;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.example.b1esimageweb.web.dto.UserInfoDto;

@org.springframework.stereotype.Service
public class MuroService {

    UserService userService;
    GalleryService galleryService;

    public MuroService(UserService userService, GalleryService galleryService){
        this.userService = userService;
        this.galleryService = galleryService;
    }
    
    public Iterable<MuroDto> getMuro(User user, int muroSize) {
        
        List<MuroDto> muroDto = new ArrayList<>();

        Iterable<User> followedUsers = user.getFollowing();
        
        for(User followedUser: followedUsers){
            if(muroDto.size()==muroSize){
                break;
            }
            Gallery gallery = userService.getGalleryByUser(followedUser);
            PhotoDto profilePhoto = userService.getPhotoProfileByUser(followedUser);
            Iterable<PhotoDto> photos = galleryService.getPhotosByGallery(gallery);
            UserInfoDto userInfoDto = new UserInfoDto(followedUser.getUserId(), followedUser.getUsername(), followedUser.getUserEmail(), null, followedUser.getDescription(), gallery, profilePhoto, followedUser.isAccountNonExpired(), followedUser.isAccountNonLocked(), followedUser.isCredentialsNonExpired(), followedUser.isEnabled(), followedUser.getAuthorities());
            for (PhotoDto photo : photos) {
                if(muroDto.size()==muroSize){
                    break;
                }
                MuroDto muroDtoElement = new MuroDto(userInfoDto, photo); 
                muroDto.add(muroDtoElement);
            }
        }

        if (muroDto.size()!=muroSize){
            int numberOfPhotosLeft = muroSize - muroDto.size();
            Iterable<PhotoDto> randomPhotos = galleryService.getRandomNumberOfPhotos(numberOfPhotosLeft);
           
            for (PhotoDto photo : randomPhotos) {
                Gallery gallery = photo.getGallery();
                User ownerUser = userService.getUserByGallery(gallery);
                PhotoDto profilePhoto = userService.getPhotoProfileByUser(ownerUser);
                UserInfoDto userInfoDto = new UserInfoDto(ownerUser.getUserId(), ownerUser.getUsername(), ownerUser.getUserEmail(), null, ownerUser.getDescription(), gallery, profilePhoto, ownerUser.isAccountNonExpired(), ownerUser.isAccountNonLocked(), ownerUser.isCredentialsNonExpired(), ownerUser.isEnabled(), ownerUser.getAuthorities());
                MuroDto muroDtoElement = new MuroDto(userInfoDto, photo); 
                muroDto.add(muroDtoElement);
            }
        }
        System.out.println(muroDto.size());
        return muroDto;
    }
}
