package com.example.b1esimageweb.web.dto;

import org.springframework.web.multipart.MultipartFile;


public class AlbumDto {

    private String name;
    private String description;
    private MultipartFile coverPhoto;

    public AlbumDto(){}

    public AlbumDto(String name, String description, MultipartFile coverPhoto) {
        this.name = name;
        this.description = description;
        this.coverPhoto = coverPhoto;
    }

    public MultipartFile getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(MultipartFile coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
