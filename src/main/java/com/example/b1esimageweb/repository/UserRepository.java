package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
    @Query("SELECT u.gallery FROM User u WHERE u.userId = :userId")
    Gallery getGalleryByUserId(@Param("userId") Integer userId);
    @Query("SELECT u.profilePicture FROM User u WHERE u.userId = :userId")
    Photo getPhotoProfileByUserId(@Param("userId") Integer userId);
    Iterable<User> findByUsernameContaining(String searchCriteria);
}
