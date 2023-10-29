package com.example.b1esimageweb.web.controller;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.service.GalleryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path="/gallery")
public class GalleryController {

    private final GalleryService service;

    public GalleryController (GalleryService service){
        this.service = service;
    }

    @PostMapping(path="/uploadPhotoGalery/{galleryId}")
    public ResponseEntity<String> uploadPhotoGallery(@PathVariable("galleryId") Integer galleryId, @RequestParam("photo") MultipartFile photo) {
        service.addNewPhoto(galleryId, photo);
        return new ResponseEntity<>("Photography upload successfully", HttpStatus.OK);
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<Iterable<Photo>> getAllPhotos(){
        Iterable<Photo> photos = service.getAllPhotos();
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }
    /*//Not finished
    @GetMapping(path="/viewPhoto/{photoId}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable("id") Integer id) {
        Photo photo = service.getPhotoById(id);
        if (photo == null){
            throw new PhotoNotFoundException("No photo found with the given ID");
        }
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    //Not finished 
    @GetMapping(path="/viewGalery/{userName}")
    public ResponseEntity<Iterable<Photo>> getPhotosByGallery(@PathVariable("userName") String userName) {
        Iterable<Photo> photos  = service.getPhotosByGallery(userName);
        if (photos == null){
            throw new PhotoNotFoundException("No photos found for this user");
        }
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }*/


    
}