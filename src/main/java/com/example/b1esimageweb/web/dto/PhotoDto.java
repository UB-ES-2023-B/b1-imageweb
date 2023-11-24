package com.example.b1esimageweb.web.dto;

import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.Gallery;

public class PhotoDto {

    private Integer photoId;
    private Gallery gallery;
    private String photoName;

    private Album album;
    private String photoExtension;
    private String photoDescription;
    private byte[] data; // Datos binarios de la imagen

    public PhotoDto(byte[] data, Integer photoId, Gallery gallery, String photoName,Album album, String photoExtension, String photoDescription) {
        this.photoId = photoId;
        this.data = data;
        this.gallery = gallery;
        this.photoName = photoName;
        this.album = album;
        this.photoExtension = photoExtension;
        this.photoDescription = photoDescription;
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

    public Album getAlbum() {
        return album;
    }

    public String getPhotoExtension() {
        return photoExtension;
    }

    public byte[] getData() {
        return data;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }
}
