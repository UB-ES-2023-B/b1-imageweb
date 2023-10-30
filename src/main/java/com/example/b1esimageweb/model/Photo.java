package com.example.b1esimageweb.model;
import jakarta.persistence.*;

@Entity
@Table(name="photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer photoId;
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] data; // Datos binarios de la imagen
    @ManyToOne
    private Gallery gallery;
    private String photoName;
    
    public Photo(){}

    public Photo(byte[] data, Gallery gallery, String photoName) {
        this.data = data;
        this.gallery = gallery;
        this.photoName = photoName;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    

}
