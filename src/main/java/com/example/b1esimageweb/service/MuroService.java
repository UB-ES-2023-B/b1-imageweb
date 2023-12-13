package com.example.b1esimageweb.service;

import java.util.ArrayList;
import java.util.Collections;
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
    
    public Iterable<MuroDto> getMuro(User user) {
        
        List<MuroDto> muroDto = new ArrayList<>();

        Iterable<User> followedUsers = user.getFollowing();
        
        for(User followedUser: followedUsers){
            if(muroDto.size()==25){
                break;
            }
            Gallery gallery = userService.getGalleryByUser(followedUser);
            PhotoDto profilePhoto = userService.getPhotoProfileByUser(followedUser);
            Iterable<PhotoDto> photos = galleryService.getPhotosByGallery(gallery);
            UserInfoDto userInfoDto = new UserInfoDto(followedUser.getUserId(), followedUser.getUsername(), followedUser.getUserEmail(), null, followedUser.getDescription(), gallery, profilePhoto, followedUser.isAccountNonExpired(), followedUser.isAccountNonLocked(), followedUser.isCredentialsNonExpired(), followedUser.isEnabled(), followedUser.getAuthorities());
            for (PhotoDto photo : photos) {
                if(muroDto.size()==25){
                    break;
                }
                MuroDto muroDtoElement = new MuroDto(userInfoDto, photo); 
                muroDto.add(muroDtoElement);
            }
        }

        if (muroDto.size()!=25){
            int numberOfPhotosLeft = 25 - muroDto.size();
            Iterable<PhotoDto> randomPhotos = galleryService.getRandomNumberOfPhotos(numberOfPhotosLeft, user);
            Collections.shuffle((List<PhotoDto>)randomPhotos);
            for (PhotoDto photo : randomPhotos) {
                boolean photoExists = false;
                for(MuroDto mu : muroDto){
                    if (photo.getPhotoId() == mu.getPhotoDto().getPhotoId()){
                        photoExists = true;
                        break;
                    }
                }
                if(!photoExists) {
                    Gallery gallery = photo.getGallery();
                    User ownerUser = userService.getUserByGallery(gallery);
                    PhotoDto profilePhoto = userService.getPhotoProfileByUser(ownerUser);
                    UserInfoDto userInfoDto = new UserInfoDto(ownerUser.getUserId(), ownerUser.getUsername(), ownerUser.getUserEmail(), null, ownerUser.getDescription(), gallery, profilePhoto, ownerUser.isAccountNonExpired(), ownerUser.isAccountNonLocked(), ownerUser.isCredentialsNonExpired(), ownerUser.isEnabled(), ownerUser.getAuthorities());
                    MuroDto muroDtoElement = new MuroDto(userInfoDto, photo);
                    muroDto.add(muroDtoElement);
                }
            }
        }

        if (muroDto.size()!=25){
            User admin = userService.getUserByUserName("adminUser");
            Gallery adminGallery = userService.getGalleryByUser(admin);
            Iterable<PhotoDto> photos = galleryService.getPhotosByGallery(adminGallery);
            Collections.shuffle((List<PhotoDto>)photos);
            UserInfoDto adminUserInfoDto = new UserInfoDto(admin.getUserId(), admin.getUsername(), admin.getUserEmail(), null, admin.getDescription(), adminGallery, null, admin.isAccountNonExpired(), admin.isAccountNonLocked(), admin.isCredentialsNonExpired(), admin.isEnabled(), admin.getAuthorities());
            for (PhotoDto p : photos){
                if(muroDto.size()==25){
                    break;
                }
                MuroDto muroDtoElement = new MuroDto(adminUserInfoDto, p);
                muroDto.add(muroDtoElement);
            }
        }
        System.out.println(muroDto.size());
        return muroDto;
    }
}
