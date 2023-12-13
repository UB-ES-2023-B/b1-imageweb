package com.example.b1esimageweb.web.dto;

public class MuroDto {

    UserInfoDto userInfoDto;
    PhotoDto photoDto;
    
    public MuroDto(UserInfoDto userInfoDto, PhotoDto photoDto) {
        this.userInfoDto = userInfoDto;
        this.photoDto = photoDto;
    }

    public UserInfoDto getUserInfoDto() {
        return userInfoDto;
    }

    public PhotoDto getPhotoDto() {
        return photoDto;
    }
    
    
}
