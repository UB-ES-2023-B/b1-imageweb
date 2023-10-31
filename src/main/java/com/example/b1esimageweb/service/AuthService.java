package com.example.b1esimageweb.service;

import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Role;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.web.Jwt.JwtTokenProvider;
import com.example.b1esimageweb.web.controller.AuthResponse;
import com.example.b1esimageweb.web.dto.UserLoginDto;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private GalleryRepository galleryRepository;
    private final JwtTokenProvider jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(UserLoginDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        UserDetails user=userRepository.findByUsername(userDto.getUsername()).orElseThrow();
        String token=jwtService.createToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse register(UserRegistrationDto userDto) {
        Gallery gallery = new Gallery();
        gallery.setGalleryName("My first Gallery");
        galleryRepository.save(gallery);
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER)
                .build();
        user.setGallery(gallery);
        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.createToken(user))
                .build();

    }
}
