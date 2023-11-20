package com.example.b1esimageweb.web.dto;

public class PhotoUpdateDto {
    String photoName;
    String photoDescription;

    public PhotoUpdateDto (String photoName, String photoDescription) {
        this.photoName = photoName;
        this.photoDescription = photoDescription;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
