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
        User user=userRepository.findByUsername("adminUser").orElseThrow();
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


        String jsonRegister = "{ \"username\": \"testUser5\", \"password\": \"testPassword\", \"email\": \"test25657@example.com\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content(jsonRegister))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        String jsonRequest = "{ \"username\": \"adminUser\", \"password\": \"password\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()); // This prints the response for debugging

        MvcResult result = mockMvc.perform(get("/user/getByUserName/{userName}", "testUser5"))  // Replace "username" with an actual username
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonUser = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonUser);
        int id = jsonNode.get("userId").asInt();
        System.out.println(id);

        String requestBody = "{\"username\":\"testUser5\",\"email\":\"newEmail22@example.com\"}";

        mockMvc.perform(put("/user/update/{username}", "testUser5")  // Replace "existingUsername" with an actual username
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        MockMultipartFile file = new MockMultipartFile("profilePhoto", "../../images/sample.jpg", MediaType.IMAGE_JPEG_VALUE, "Some Content".getBytes());

        mockMvc.perform(multipart("/user/uploadPhotoProfile")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/user/delete/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))  // Replace 1 with an actual user ID
                .andExpect(status().isOk());

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

    /*
    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/delete/{id}", 1))  // Replace 1 with an actual user ID
                .andExpect(status().isOk());
    }
     */

}
