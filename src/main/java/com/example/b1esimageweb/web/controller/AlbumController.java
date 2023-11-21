package com.example.b1esimageweb.web.controller;


import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.service.AlbumService;
import com.example.b1esimageweb.web.dto.AlbumDto;
import com.example.b1esimageweb.web.responses.AlbumResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AlbumController {

    private AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }
    @PostMapping("/newAlbum")
    public ResponseEntity<Object> createAlbum(@RequestParam("album") String albumDtoAsString, @RequestParam("coverPhoto") MultipartFile coverPhoto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AlbumDto albumDto = objectMapper.readValue(albumDtoAsString, AlbumDto.class);
            albumDto.setCoverPhoto(coverPhoto);
            Album newAlbum = albumService.createAlbum(albumDto);
            return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("No se ha podido crear un album",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAlbums")
    public ResponseEntity<AlbumResponse> getUserAlbums() {
        try {
            Iterable<Album> albums = albumService.getAllAlbumsForUser();
            if (albums == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            AlbumResponse response = new AlbumResponse();
            response.setAlbums(albums);
            response.setLength(IterableUtils.size(albums));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addPhotosAlbum/{albumId}")
    public ResponseEntity<?> addPhotosToAlbum(@PathVariable int albumId, @RequestParam("photos") List<MultipartFile> photos) {
        try {
            Album album = albumService.addPhotosToAlbum(albumId, photos);
            return new ResponseEntity<>(album, HttpStatus.OK);
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