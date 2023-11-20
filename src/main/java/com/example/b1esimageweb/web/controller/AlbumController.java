package com.example.b1esimageweb.web.controller;


import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.service.AlbumService;
import com.example.b1esimageweb.web.dto.AlbumDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AlbumController {

    private AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }


    @PostMapping("/newAlbum")
    public ResponseEntity<Object> createAlbum(@RequestBody AlbumDto albumDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Album newAlbum = albumService.createAlbum(albumDto);
            return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("No se ha podido crear este album",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
