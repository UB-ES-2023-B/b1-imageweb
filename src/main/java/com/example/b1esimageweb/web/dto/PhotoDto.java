package com.example.b1esimageweb.web.dto;

import java.util.List;

public class PhotoDto {

    private List<Integer> photoIds;

    public PhotoDto(List<Integer> photoIds) {
        this.photoIds = photoIds;
    }

    public List<Integer> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Integer> photoIds) {
        this.photoIds = photoIds;
    }
}
