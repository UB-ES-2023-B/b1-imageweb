package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
    @Query("SELECT u.gallery FROM User u WHERE u.userId = :userId")
    Gallery getGalleryByUserId(@Param("userId") Integer userId);
    @Query("SELECT u.userId FROM User u WHERE u.gallery = :gallery")
    Integer getUserByGallery(@Param("gallery") Gallery gallery);
    @Query("SELECT u.profilePicture FROM User u WHERE u.userId = :userId")
    Photo getPhotoProfileByUserId(@Param("userId") Integer userId);
    Iterable<User> findByUsernameContaining(String searchCriteria);

    /*@Query("SELECT u FROM User u LEFT JOIN FETCH u.followers WHERE u.username = :username")
    Optional<User> findByUsernameWithFollowers(@Param("username") String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.following WHERE u.username = :username")
    Optional<User> findByUsernameWithFollowing(@Param("username") String username);
    */
}
