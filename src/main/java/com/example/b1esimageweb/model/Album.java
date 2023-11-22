package com.example.b1esimageweb.model;

import com.example.b1esimageweb.web.dto.PhotoDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer albumId;
    private String albumName;
    @Size(max = 25, message = "Description must not exceed 25 words")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Album(){}
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
