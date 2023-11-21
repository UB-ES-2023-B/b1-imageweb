package com.example.b1esimageweb.web.responses;

import com.example.b1esimageweb.model.Album;

public class AlbumResponse {
    private Iterable<Album> albums;
    private int lenAlbums;
    public void setAlbums(Iterable<Album> albums) {
        this.albums = albums;
    }

    public int getLength() {
        return lenAlbums;
    }

    public void setLength(int lenAlbums) {
        this.lenAlbums = lenAlbums;
    }

    public Iterable<Album> getAlbums() {
        return albums;
    }
}
