package com.example.b1esimageweb.service;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

import java.util.List;
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
        List<UserInfoDto> userInfoDtos = new ArrayList<>();

        User currentUser = service.getCurrentUserFromConext();
        Iterable<User> followedUsers = currentUser.getFollowing();

        users.forEach(user -> {
            PhotoDto profilePhoto = service.getPhotoProfileByUser(user);
            UserInfoDto userInfoDto = new UserInfoDto(user.getUserId(), user.getUsername(), user.getUserEmail(), user.getPassword(), user.getDescription(), user.getGallery(), profilePhoto, user.isAccountNonExpired(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isEnabled(), user.getAuthorities());
            
            if (StreamSupport.stream(followedUsers.spliterator(), false).anyMatch(u -> u.getUserId().equals(user.getUserId()))){
                System.out.println(userInfoDtos);
                userInfoDtos.add(0, userInfoDto);
                System.out.println(userInfoDtos);
            }else{
                userInfoDtos.add(userInfoDto);
            }
        }
        );
        return userInfoDtos;
    }
}
