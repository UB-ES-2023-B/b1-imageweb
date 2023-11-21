package com.example.b1esimageweb.service;

import org.springframework.stereotype.Service;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserRepository;

@Service
public class SearchService {

    private final UserRepository userRepository;

    public SearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsersSearchResults(String searchCriteria){
        return userRepository.findByUsernameContaining(searchCriteria);
    }
}
