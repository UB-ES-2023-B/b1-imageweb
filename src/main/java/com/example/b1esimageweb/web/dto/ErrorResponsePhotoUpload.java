package com.example.b1esimageweb.web.dto;

import java.util.List;

public class ErrorResponsePhotoUpload {
    
    private String error;
    private List<Integer> photoIds;

    public ErrorResponsePhotoUpload(String error, List<Integer> photoIds){
        this.error = error;
        this.photoIds = photoIds;
    }

    public String getError() {
        return error;
    }

    public List<Integer> getPhotoIds() {
        return photoIds;
    }


}
