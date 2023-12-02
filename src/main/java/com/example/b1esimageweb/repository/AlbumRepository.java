package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.User;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, Integer> {

    Iterable<Album> findAllByUser(User user);
}
