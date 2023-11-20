package com.example.b1esimageweb;

import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.service.GalleryService;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.Jwt.JwtTokenProvider;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebSecurity
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GalleryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    private String userToken;

    public GalleryControllerIntegrationTest() {
    }


    @BeforeAll
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void setUp() {
        // Create a test user
        User user=userRepository.findByUsername("adminUser").orElseThrow();
        userToken = tokenProvider.createToken(user);
    }

    @Test
    @Order(1)
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void testUserGallery() throws Exception {
        MvcResult result = mockMvc.perform(get("/gallery/viewGalleryFromUser/{userName}", "adminUser")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        /*
        String jsonGallery = result.getResponse().getContentAsString();
        System.out.println(jsonGallery);
        Gallery gallery = new ObjectMapper().readValue(jsonGallery, Gallery.class);

        int id = gallery.getGalleryId();

        mockMvc.perform(get("/gallery/viewGallery/{galleryId}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        MockMultipartFile file = new MockMultipartFile("testPhoto", "../../images/sample.jpg", MediaType.IMAGE_JPEG_VALUE, "Some Content".getBytes());

        mockMvc.perform(multipart("/uploadPhotoGalery/{galleryId}", id)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk());
        */
    }



}
