package com.example.b1esimageweb.model;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer photoId;
    @ManyToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    @ManyToMany
    @JoinTable(
        name = "photo_album",
        joinColumns = @JoinColumn(name = "photo_id"),
        inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private Set<Album> albums = new HashSet<>();
    
    private String photoName;

    public Set<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    private String photoExtension;
    @Size(max = 25, message = "Description must not exceed 25 words")
    private String photoDescription;

    public Photo(){}

    public Photo(Gallery gallery, String photoName, String photoExtension, String photoDescription) {
        this.gallery = gallery;
        this.photoName = photoName;
        this.photoExtension = photoExtension;
        this.photoDescription = photoDescription;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int id ) {
        this.photoId = id;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoExtension() {
        return photoExtension;
    }

    public void setPhotoExtension(String photoExtension) {
        this.photoExtension = photoExtension;
    }


    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }
}
