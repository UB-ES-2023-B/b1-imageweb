package com.example.b1esimageweb.web.dto;

import com.example.b1esimageweb.model.Photo;

import java.util.List;

public class AlbumDto {

    private String name;
    private String description;

    public AlbumDto(String name, String description) {
        this.name = name;
        this.description = description;
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
