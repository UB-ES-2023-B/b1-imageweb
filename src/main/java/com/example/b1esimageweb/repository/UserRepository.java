package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUserEmail(String email);
    User getUserByUserName(String userName);
    boolean existsUserByUserEmail(String email);
    boolean existsUserByUserName(String userName);
    
    @Query("SELECT u.gallery FROM User u WHERE u.id = :userId")
    Gallery getGalleryByUserId(@Param("userId") Integer userId);


}
