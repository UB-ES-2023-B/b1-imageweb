package com.example.b1esimageweb.web.responses;

import com.example.b1esimageweb.web.dto.PhotoDto;

import java.util.Collection;
import java.util.List;

public class AlbumResponse {
    private Collection<List<PhotoDto>> albums;
    private int lenAlbums;
    public void setAlbums(Collection<List<PhotoDto>> albums) {
        this.albums = albums;
    }

    public int getLength() {
        return lenAlbums;
    }

    public void setLength(int lenAlbums) {
        this.lenAlbums = lenAlbums;
    }

    public Collection<List<PhotoDto>> getAlbums() {
        return albums;
    }
}
