package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findUserByUserEmail(String email);

    User getUserByUserName(String username);

    boolean existsUserByUserEmail(String email);
    boolean existsUserByUserName(String userName);

}
