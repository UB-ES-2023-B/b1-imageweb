package com.example.b1esimageweb.repository;

import com.example.b1esimageweb.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUserEmail(String email);
    User getUserByUserName(String userName);
    boolean existsUserByUserEmail(String email);
    boolean existsUserByUserName(String userName);

}
