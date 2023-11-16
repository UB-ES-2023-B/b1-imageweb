package com.example.b1esimageweb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer albumId;
    private String albumName;
    @Size(max = 25, message = "Description must not exceed 25 words")
    private String description;

    public Album(){}

    public Album(String albumName) {
        this.albumName = albumName;
    }
    public Integer getAlbumId() {
        return albumId;
    }
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

}
