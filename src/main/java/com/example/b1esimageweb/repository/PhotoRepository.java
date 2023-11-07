package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.Photo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.b1esimageweb.model.Gallery;


public interface PhotoRepository extends CrudRepository<Photo, Integer> {

    Iterable<Photo> findByGallery(Gallery gallery);
    
}
