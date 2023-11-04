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
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;
    private String photoName;
    private String photoExtension;
    
    public Photo(){}

    public Photo(byte[] data, Gallery gallery, String photoName, String photoExtension) {
        this.data = data;
        this.gallery = gallery;
        this.photoName = photoName;
        this.photoExtension = photoExtension;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int id ) {
        this.photoId = id;
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

    public String getPhotoExtension() {
        return photoExtension;
    }

    public void setPhotoExtension(String photoExtension) {
        this.photoExtension = photoExtension;
    }

    

}
