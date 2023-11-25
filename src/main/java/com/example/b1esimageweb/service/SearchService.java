package com.example.b1esimageweb.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.example.b1esimageweb.web.dto.UserInfoDto;

@Service
public class SearchService {

    private final UserRepository userRepository;
    private final UserService service;

    public SearchService(UserRepository userRepository, UserService service) {
        this.userRepository = userRepository;
        this.service = service;
    }

    public Iterable<UserInfoDto> getUsersSearchResults(String searchCriteria){
        Iterable<User> users = userRepository.findByUsernameContaining(searchCriteria);
        return getUsersDTOSearchResults(users);
    }

    private Iterable<UserInfoDto> getUsersDTOSearchResults (Iterable<User> users){
        ArrayList<UserInfoDto> userInfoDtos = new ArrayList<>();
        users.forEach(user -> {
            PhotoDto profilePhoto = service.getPhotoProfileByUser(user);
            UserInfoDto userInfoDto = new UserInfoDto(user.getUserId(), user.getUsername(), user.getUserEmail(), user.getPassword(), user.getDescription(), user.getGallery(), profilePhoto, user.isAccountNonExpired(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isEnabled(), user.getAuthorities());
            userInfoDtos.add(userInfoDto);
        }
        );
        return userInfoDtos;
    }
}
