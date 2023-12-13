package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.Photo;

import com.example.b1esimageweb.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.b1esimageweb.model.Gallery;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PhotoRepository extends CrudRepository<Photo, Integer> {

    Iterable<Photo> findByGallery(Gallery gallery);
    Iterable<Photo> findByAlbumsContaining(Album album);

    void deleteByAlbumsContaining(Album allbum);

    int countByGallery(Gallery gallery);
}
