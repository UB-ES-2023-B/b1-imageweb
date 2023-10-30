package com.example.b1esimageweb.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.example.b1esimageweb.Exceptions.GalleryNotFoundException;
import com.example.b1esimageweb.Exceptions.PhotoNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.PhotoRepository;

@org.springframework.stereotype.Service
public class GalleryService {
    
    private PhotoRepository photoRepository;
    private GalleryRepository galleryRepository;
    @Autowired
    public GalleryService(PhotoRepository photoRepository, GalleryRepository galleryRepository){
        this.photoRepository = photoRepository;
        this.galleryRepository=galleryRepository;
    }

    public Photo addNewPhoto (Integer galleryId, MultipartFile photo){
        Gallery gallery = galleryRepository.findById(galleryId).orElseThrow(() -> new GalleryNotFoundException("Gallery with id " + galleryId + " not found"));
        
        Photo newPhoto = new Photo();
        try {
            newPhoto.setData(photo.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        newPhoto.setPhotoName(photo.getOriginalFilename());
        newPhoto.setGallery(gallery);

        return photoRepository.save(newPhoto);
    }

    public Iterable<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Photo getPhotoById(int id) {
        return photoRepository.findById(id).orElseThrow(()-> new PhotoNotFoundException("Photo with id " + id + "not found"));
    }

    /*public Iterable<Photo> getPhotosByGallery(int id) {
        
    }*/

}
