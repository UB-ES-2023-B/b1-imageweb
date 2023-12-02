package com.example.b1esimageweb.web.controller;


import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.AlbumService;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.dto.AlbumDto;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.example.b1esimageweb.web.responses.AlbumResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                Map<Integer, List<PhotoDto>> map = albumService.getAllAlbumsForUser(currentUser);
                if (map.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                AlbumResponse response = new AlbumResponse();
                response.setAlbums(map.values());
                response.setLength(map.keySet().size());
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
            Map<Integer, List<PhotoDto>> map = albumService.getAllAlbumsForUser(user);
            if (map.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            AlbumResponse response = new AlbumResponse();
            response.setAlbums(map.values());
            response.setLength(map.keySet().size());
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
        try {
            albumService.updateAlbum(albumId,dto);
            return new ResponseEntity<>("Album successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Album could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}