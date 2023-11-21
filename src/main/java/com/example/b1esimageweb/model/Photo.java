package com.example.b1esimageweb.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
    private String photoName;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
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
