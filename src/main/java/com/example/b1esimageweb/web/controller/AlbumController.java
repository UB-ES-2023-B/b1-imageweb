package com.example.b1esimageweb.web.controller;


import com.example.b1esimageweb.Exceptions.UnauthorizedAlbumDeletionException;
import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.AlbumService;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.dto.AlbumDto;
import com.example.b1esimageweb.web.dto.ErrorResponsePhotoUpload;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.example.b1esimageweb.web.dto.PhotosDto;
import com.example.b1esimageweb.web.responses.AlbumResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AlbumController {

    private AlbumService albumService;
    private UserService userService;

    public AlbumController(AlbumService albumService, UserService userService) {
        this.albumService = albumService;
        this.userService = userService;
    }

    @PostMapping("/newAlbum")
    public ResponseEntity<Object> createAlbum(@RequestParam("album") String albumDtoAsString, @RequestParam("coverPhoto") MultipartFile coverPhoto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AlbumDto albumDto = objectMapper.readValue(albumDtoAsString, AlbumDto.class);
            if (coverPhoto.getSize() > 2 * 1024 * 1024) { // 2MB in bytes
                return new ResponseEntity<>("Photo with name "+ coverPhoto.getOriginalFilename() + " exceeds the maximum size allowed (2MB)", HttpStatus.BAD_REQUEST);
            }
            albumDto.setCoverPhoto(coverPhoto);
            Album album= albumService.createAlbum(albumDto);
            Iterable<PhotoDto> photos = albumService.getPhotosByAlbum(album);
            return new ResponseEntity<>(photos, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("No se ha podido crear un album",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAlbums")
    public ResponseEntity<Object> getUserAlbums() {
        try {
            Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = null;
            if(obj instanceof User){
                currentUser = (User) obj;
            }
            if(currentUser != null) {
                Map<Album, PhotoDto> map = albumService.getAllAlbumsForUser(currentUser);
                if (map.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                List<AlbumResponse> response = new ArrayList<>();
                for (Map.Entry<Album, PhotoDto> entry : map.entrySet()) {
                    Album album = entry.getKey();
                    PhotoDto photoDto = entry.getValue();
                    
                    AlbumResponse albumResponse = new AlbumResponse(photoDto, album.getAlbumName(), album.getDescription(), album.getAlbumId(), albumService.getAlbumSize(album));
                    response.add(albumResponse);
                }
                return new ResponseEntity<>(response, HttpStatus.OK);

            }else{
                throw new UserNotFoundException("User was not found");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAlbums/{username}")
    public ResponseEntity<Object> getAlbumsForAUser(@PathVariable String username) {
        try {
            User user = userService.getUserByUserName(username);
            Map<Album, PhotoDto> map = albumService.getAllAlbumsForUser(user);
            if (map.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<AlbumResponse> response = new ArrayList<>();
            for (Map.Entry<Album, PhotoDto> entry : map.entrySet()) {
                Album album = entry.getKey();
                PhotoDto photoDto = entry.getValue();
                
                AlbumResponse albumResponse = new AlbumResponse(photoDto, album.getAlbumName(), album.getDescription(), album.getAlbumId(), albumService.getAlbumSize(album));
                response.add(albumResponse);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAlbum/{albumId}")
    public Iterable<PhotoDto> getAlbumById(@PathVariable int albumId) {
        Album album = albumService.getAlbumById(albumId);
        Iterable<PhotoDto> photos = albumService.getPhotosByAlbum(album);
        return photos;
    }

    @PostMapping("/addPhotosAlbum/{albumId}")
    public ResponseEntity<?> addPhotosToAlbum(@PathVariable int albumId, @RequestParam("photos") List<MultipartFile> photosList) {
        if(!albumService.isAlbumOwner(albumId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        for (MultipartFile photo : photosList) {
            if (photo.getSize() > 2 * 1024 * 1024) { // 2MB in bytes
                return new ResponseEntity<>("Photo with name "+ photo.getOriginalFilename() + " exceeds the maximum size allowed (2MB)", HttpStatus.BAD_REQUEST);
            }
        }
        try {
            Album album = albumService.addPhotosToAlbum(albumId, photosList);
            Iterable<PhotoDto> photos = albumService.getPhotosByAlbum(album);
            return new ResponseEntity<>(photos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Could not add photos to album", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/album/updateInfo/{albumId}")
    public ResponseEntity<?> updateAlbum(@PathVariable int albumId, @RequestBody AlbumDto dto){
        if(!albumService.isAlbumOwner(albumId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            albumService.updateAlbum(albumId,dto);
            return new ResponseEntity<>("Album successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Album could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/uploadPhotoGaleryToAlbum")
    public ResponseEntity<?> uploadPhotoGalleryToAlbum(@RequestBody Map<String, Object> requestBody) {
        int albumId = (int) requestBody.get("albumId");
        Album album = albumService.getAlbumById(albumId);

        Iterable<Integer> photoIds = (Iterable<Integer>) requestBody.get("photoIds");

        List<Integer> photoIdsRepited = albumService.checkAlbumForPhotos(album, photoIds);
        if (!photoIdsRepited.isEmpty()) {
            ErrorResponsePhotoUpload errorResponsePhotoUpload = new ErrorResponsePhotoUpload("Photos already included", photoIdsRepited);
            return new ResponseEntity<>(errorResponsePhotoUpload, HttpStatus.BAD_REQUEST);
        }
        album = albumService.addPhotosToAlbumFromGallery(albumId, photoIds);
        Iterable<PhotoDto> photos = albumService.getPhotosByAlbum(album);
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @DeleteMapping(path = "/deletephotos/{albumId}")
    public ResponseEntity<Map<String, String>> deletePhotosFromAlbum(@PathVariable int albumId, @RequestBody PhotosDto photoDto) {
        Map<String, String> response = new HashMap<>();
        String msg = "";
        try {
            List<Integer> photoIds = photoDto.getPhotoIds();
            if(photoIds.size() == 0){
                response.put("message", "No photos to delete");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            msg = albumService.deleteAlbumPhotos(albumId, photoIds);
            response.put("message", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", "Photos not found");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/albumInfo/{username}")
    public ResponseEntity<?> getUserAlbumInfo(@PathVariable String username){
        try{
            User user = userService.getUserByUserName(username);
            Map<Integer, String> info = albumService.getUserAlbumsNamesAndIds(user);
            return new ResponseEntity<>(info, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error getting album info" ,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/album/delete")
    public ResponseEntity<?> deleteAlbums(@RequestBody Map<String, List<Integer>> albumIdsMap) {
        Map<String, String> response = new HashMap<>();
        try {
            List<Integer> albumIds = albumIdsMap.get("albumIds");
            if (albumIds == null || albumIds.isEmpty()) {
                response.put("message", "No album IDs provided");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            albumService.deleteAlbumsByIds(albumIds);
            response.put("message", "Albums deleted successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UnauthorizedAlbumDeletionException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            response.put("message", "Error deleting albums: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}