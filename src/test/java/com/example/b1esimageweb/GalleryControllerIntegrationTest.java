package com.example.b1esimageweb;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.web.Jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    public void setUp() {
        // Create a test user
        User user=userRepository.findByUsername("testing23Marc").orElseThrow();
        userToken = tokenProvider.createToken(user);
    }

    @Test
    @Order(1)
    @WithMockUser (username = "testing23Marc", password = "1234Asd.")
    public void testUserGallery() throws Exception {
        /*
        String jsonRequest = "{ \"username\": \"testing23Marc\", \"password\": \"1234Asd.\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        */
        MvcResult result = mockMvc.perform(get("/user/getByUserName/{userName}", "testing23Marc"))  // Replace "username" with an actual username
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonUser = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonUser);
        JsonNode test = jsonNode.get("gallery");
        int id = test.get("galleryrId").asInt();
        System.out.println("Gallery ID" + id);

        mockMvc.perform(get("/gallery/viewGallery/{galleryId}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String imagePath = "images/sample.jpg";

        // Resolve the absolute path to the image file
        Path absolutePath = Paths.get("src/test", imagePath);

        // Read image file into a byte array
        byte[] imageBytes = Files.readAllBytes(absolutePath);

        MockMultipartFile file = new MockMultipartFile("photo", "sample.jpg", MediaType.IMAGE_JPEG_VALUE, imageBytes);

        
        mockMvc.perform(multipart("/gallery/uploadPhotoGalery/{galleryId}", id)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk());


        MvcResult jsonPhotos = mockMvc.perform(get("/gallery/viewGalleryFromUser/{userName}", "testing23Marc")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonGallery = jsonPhotos.getResponse().getContentAsString();
        System.out.println(jsonGallery);
        ObjectMapper objectMapper2 = new ObjectMapper();
        JsonNode jsonNode2 = objectMapper2.readTree(jsonGallery);
        JsonNode lastObject = jsonNode2.get(jsonNode2.size() - 1);
        int photoId = lastObject.get("photoId").asInt();
        List<Integer> photoIds = new ArrayList<>();
        photoIds.add(photoId);
        System.out.println(photoIds);

        JsonNode jsonNodeDelete = objectMapper.createObjectNode()
                .putPOJO("photoIds", photoIds);

        String photoIdsJson = objectMapper.writeValueAsString(jsonNodeDelete);


        mockMvc.perform(delete("/gallery/deletephotos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(photoIdsJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk());


    }



}
