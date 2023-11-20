package com.example.b1esimageweb.web.dto;

import java.util.List;

public class PhotosDto {

    private List<Integer> photoIds;

    public PhotosDto() {
    }

    public List<Integer> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Integer> photoIds) {
        this.photoIds = photoIds;
    }
}
