package com.example.b1esimageweb.model;
import jakarta.persistence.*;

@Entity
@Table(name="galleries")
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer galleryId;
    private String galleryName;
    
    public Gallery(){}

    public Gallery(String galleryName) {
        this.galleryName = galleryName;
    }
    public Integer getGalleryrId() {
        return galleryId;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }
}
