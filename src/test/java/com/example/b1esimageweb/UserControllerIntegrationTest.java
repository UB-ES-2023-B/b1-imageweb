package com.example.b1esimageweb;

import com.example.b1esimageweb.repository.UserRepository;
import com.example.b1esimageweb.service.UserService;
import com.example.b1esimageweb.web.Jwt.JwtTokenProvider;
import com.example.b1esimageweb.web.dto.UserRegistrationDto;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void setUp() {
        // Create a test user
        UserDetails user=userRepository.findByUsername("adminUser").orElseThrow();
        userToken = tokenProvider.createToken(user);
    }

    /*
    @Test
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void testRegisterUser() throws Exception {
        String jsonRequest = "{ \"username\": \"testUser\", \"password\": \"testPassword\", \"email\": \"test@example.com\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()); // This prints the response for debugging
    }
    */

    @Test
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void testLoginUser() throws Exception {
        String jsonRequest = "{ \"username\": \"testUser\", \"password\": \"testPassword\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()); // This prints the response for debugging
    }

    @Test
    @Order(1)
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/user/getAll")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    @Order(2)
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/user/getById/{id}", 1))  // Replace 1 with an actual user ID
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(3)
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void testGetUserByUserName() throws Exception {
        mockMvc.perform(get("/user/getByUserName/{userName}", "testUser"))  // Replace "username" with an actual username
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    @Order(4)
    @WithMockUser (username = "adminUser", password = "password", roles = "ADMIN")
    public void testUpdateUser() throws Exception {
        // Prepare a JSON request body for updating a user
        String requestBody = "{\"username\":\"testUser\",\"email\":\"newEmail2@example.com\"}";

        mockMvc.perform(put("/user/update/{username}", "testUser")  // Replace "existingUsername" with an actual username
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /*
    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/delete/{id}", 1))  // Replace 1 with an actual user ID
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadUserProfilePhoto() throws Exception {
        // Create a sample file for profile picture upload
        MockMultipartFile file = new MockMultipartFile("profilePhoto", "sample.jpg", MediaType.IMAGE_JPEG_VALUE, "Some Content".getBytes());

        mockMvc.perform(multipart("/user/uploadPhotoProfile")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

 */
}
