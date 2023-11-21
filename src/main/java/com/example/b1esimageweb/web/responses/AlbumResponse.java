package com.example.b1esimageweb.web.responses;

import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.web.dto.PhotoDto;

public class AlbumResponse {
    private Iterable<PhotoDto> albums;
    private int lenAlbums;
    public void setAlbums(Iterable<PhotoDto> albums) {
        this.albums = albums;
    }

    public int getLength() {
        return lenAlbums;
    }

    public void setLength(int lenAlbums) {
        this.lenAlbums = lenAlbums;
    }

    public Iterable<PhotoDto> getAlbums() {
        return albums;
    }
}
