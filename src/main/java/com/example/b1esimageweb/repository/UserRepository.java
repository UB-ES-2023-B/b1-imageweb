package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findUserByEmail(String email);

    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);

}
