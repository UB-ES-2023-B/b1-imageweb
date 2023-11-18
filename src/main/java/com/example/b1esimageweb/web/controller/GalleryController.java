package com.example.b1esimageweb.web.controller;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.GalleryService;
import com.example.b1esimageweb.service.UserService;

import com.example.b1esimageweb.web.dto.PhotoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/gallery")
public class GalleryController {

    private final GalleryService galleryService;
    private final UserService userService;

    public GalleryController (GalleryService galleryService, UserService userService){
        this.galleryService = galleryService;
        this.userService = userService;
    }

    @PostMapping(path="/uploadPhotoGalery/{galleryId}")
    public ResponseEntity<String> uploadPhotoGallery(@PathVariable("galleryId") Integer galleryId, @RequestParam("photo") MultipartFile photo) {
        // Verificar el tamaño del archivo
        if (photo.getSize() > 2 * 1024 * 1024) { // 3MB en bytes
            return new ResponseEntity<>("Photo size exceeds the maximum allowed size of 2MB", HttpStatus.BAD_REQUEST);
        }
        galleryService.addNewPhoto(galleryId, photo);
        return new ResponseEntity<>("Photography upload successfully", HttpStatus.OK);
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<Iterable<Photo>> getAllPhotos(){
        Iterable<Photo> photos = galleryService.getAllPhotos();
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }
    
    @GetMapping(path="/viewPhoto/{photoId}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable("photoId") Integer id) {
        Photo photo = galleryService.getPhotoById(id);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @GetMapping(path="/viewGallery/{galleryId}")
    public ResponseEntity<Gallery> getGalleryById(@PathVariable("galleryId") Integer galleryId) {
        Gallery gallery = galleryService.getGalleryById(galleryId);
        return new ResponseEntity<>(gallery, HttpStatus.OK);
    }

    @GetMapping(path="/viewGalleryFromUser/{userName}")
    public ResponseEntity<Iterable<Photo>> getPhotosByGallery(@PathVariable("userName") String userName) {
        User user = userService.getUserByUserName(userName);
        Gallery gallery = userService.getGalleryByUser(user);
        Iterable<Photo> photos = galleryService.getPhotosByGallery(gallery);
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @DeleteMapping(path = "/deletephotos")
    public ResponseEntity<Map<String, String>> deletePhotosFromGallery(@RequestBody PhotoDto photoDto) {
        Map<String, String> response = new HashMap<>();
        String msg = "";
        try {
            List<Integer> photoIds = photoDto.getPhotoIds();
            if(photoIds.size() == 0){
                response.put("message", "No photos to delete");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            msg = galleryService.deleteGalleryPhotos(photoIds);
            response.put("message", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", "Photos not found");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}