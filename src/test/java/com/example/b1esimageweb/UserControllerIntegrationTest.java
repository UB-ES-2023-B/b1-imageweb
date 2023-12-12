package com.example.b1esimageweb;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.Jwt.JwtTokenProvider;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebSecurity
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    private String userToken;

    public UserControllerIntegrationTest() {
    }


    @BeforeAll
    public void setUp() {
        // Create a test user
        User user=userRepository.findByUsername("testing23Marc").orElseThrow();
        userToken = tokenProvider.createToken(user);
    }

    @Test
    @WithMockUser (username = "testing23Marc", password = "1234Asd.")
    public void testLoginUser() throws Exception {


        String jsonRegister = "{ \"username\": \"testUser8\", \"password\": \"testPassword\", \"email\": \"teest25@exxxample.com\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content(jsonRegister))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        User user=userRepository.findByUsername("testUser8").orElseThrow();
        userToken = tokenProvider.createToken(user);

        MvcResult result = mockMvc.perform(get("/user/getByUserName/{userName}", "testUser8"))  // Replace "username" with an actual username
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonUser = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonUser);
        int id = jsonNode.get("userId").asInt();
        System.out.println(id);

        String requestBody = "{\"username\":\"testUser8\",\"email\":\"newEmaiwww23l22@example.com\"}";

        mockMvc.perform(put("/user/update/{username}", "testUser8")  // Replace "existingUsername" with an actual username
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String imagePath = "images/sample.jpg";

        // Resolve the absolute path to the image file
        Path absolutePath = Paths.get("src/test", imagePath);

        // Read image file into a byte array
        byte[] imageBytes = Files.readAllBytes(absolutePath);

        MockMultipartFile file = new MockMultipartFile("profilePhoto", "profileSample.jpg", MediaType.IMAGE_JPEG_VALUE, imageBytes);

        mockMvc.perform(multipart("/user/uploadPhotoProfile")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/user/follow/{userToFollowUsername}", "testing23Marc")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/user/unfollow/{userToUnfollowUsername}", "testing23Marc")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(delete("/user/delete/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))  // Replace 1 with an actual user ID
                .andExpect(status().isOk());

    }
}
