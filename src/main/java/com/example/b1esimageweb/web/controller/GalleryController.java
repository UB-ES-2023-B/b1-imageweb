package com.example.b1esimageweb.web.controller;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.GalleryService;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.example.b1esimageweb.web.dto.PhotosDto;
import com.example.b1esimageweb.web.dto.PhotoUpdateDto;
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
    public ResponseEntity<?> uploadPhotoGallery(@PathVariable("galleryId") Integer galleryId, @RequestParam("photo") MultipartFile photo) {
        if(!galleryService.isGalleryOwner(galleryId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // Verificar el tamaño del archivo
        if (photo.getSize() > 2 * 1024 * 1024) { // 2MB en bytes
            return new ResponseEntity<>("Photo size exceeds the maximum allowed size of 2MB", HttpStatus.BAD_REQUEST);
        }
        PhotoDto newPhoto = galleryService.addNewPhoto(galleryId, photo);
        if(newPhoto==null){
            return new ResponseEntity<>("An error ocurred while uploading the Photo", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newPhoto, HttpStatus.OK);
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<Iterable<?>> getAllPhotos(){
        Iterable<PhotoDto> photos = galleryService.getAllPhotos();
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }
    
    @GetMapping(path="/viewPhoto/{photoId}")
    public ResponseEntity<PhotoDto> getPhotoById(@PathVariable("photoId") Integer id) {
        PhotoDto photo = galleryService.getPhotoById(id);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @GetMapping(path="/viewGallery/{galleryId}")
    public ResponseEntity<Gallery> getGalleryById(@PathVariable("galleryId") Integer galleryId) {
        Gallery gallery = galleryService.getGalleryById(galleryId);
        return new ResponseEntity<>(gallery, HttpStatus.OK);
    }

    @GetMapping(path="/viewGalleryFromUser/{userName}")
    public ResponseEntity<Iterable<PhotoDto>> getPhotosByGallery(@PathVariable("userName") String userName) {
        User user = userService.getUserByUserName(userName);
        Gallery gallery = userService.getGalleryByUser(user);
        Iterable<PhotoDto> photos = galleryService.getPhotosByGallery(gallery);
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @DeleteMapping(path = "/deletephotos")
    public ResponseEntity<Map<String, String>> deletePhotosFromGallery(@RequestBody PhotosDto photoDto) {
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

    @PutMapping(path = "/editInfoPhoto/{photoId}")
    public ResponseEntity<Map<String, String>> updatePhotoById(@PathVariable("photoId") int photoId, @RequestBody PhotoUpdateDto photoUpdateDto) {
        if(!galleryService.isPhotoOwner(photoId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Map<String, String> response = new HashMap<>();
        try {
            Photo photo = galleryService.updateInfoPhotoById(photoId, photoUpdateDto.getPhotoName(), photoUpdateDto.getPhotoDescription());
            if (photo != null) {
                response.put("message", "successful");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Photo not found.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}