package com.example.b1esimageweb.web.dto;

import com.example.b1esimageweb.model.Gallery;

public class PhotoDto {

    private Integer photoId;
    private Gallery gallery;
    private String photoName;
    private String photoExtension;
    private byte[] data; // Datos binarios de la imagen

    public PhotoDto(byte[] data, Integer photoId, Gallery gallery, String photoName, String photoExtension) {
        this.photoId = photoId;
        this.data = data;
        this.gallery = gallery;
        this.photoName = photoName;
        this.photoExtension = photoExtension;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public String getPhotoName() {
        return photoName;
    }

    public String getPhotoExtension() {
        return photoExtension;
    }

    public byte[] getData() {
        return data;
    }
    
}
