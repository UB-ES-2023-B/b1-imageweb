package com.example.b1esimageweb.web.responses;

import com.example.b1esimageweb.web.dto.PhotoDto;


public class AlbumResponse {

    private PhotoDto coverPhoto;
    private String name;
    private String description;
    private int lenAlbums;
    private int albumId;

    public AlbumResponse(PhotoDto coverPhoto, String name, String description, int albumId, int lenAlbums) {
        this.coverPhoto = coverPhoto;
        this.name = name;
        this.lenAlbums = lenAlbums;
        this.description = description;
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAlbumId() {
        return albumId;
    }

    public int getLength() {
        return lenAlbums;
    }

    public PhotoDto getcoverPhoto() {
        return coverPhoto;
    }
}
