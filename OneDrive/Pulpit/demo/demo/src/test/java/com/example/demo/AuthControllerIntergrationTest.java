package com.example.demo;

import com.example.demo.dtos.AuthRequest;
import com.example.demo.dtos.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User existingUser = new User();
        existingUser.setUsername("user");
        existingUser.setEmail("user@example.com");
        existingUser.setPassword(passwordEncoder.encode("encodedPassword")); // może być dowolny string
        existingUser.setRole("ROLE_USER");

        userRepository.save(existingUser);
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("marysia");
        request.setEmail("marysia@example.com");
        request.setPassword("securePassword123");

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void shouldReturnBadRequest_whenUsernameAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user"); // już istnieje
        request.setEmail("new@example.com");
        request.setPassword("anotherPassword");

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username: user already exists"));
    }

    @Test
    void shouldReturnBadRequest_whenEmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("user@example.com"); // już istnieje
        request.setPassword("anotherPassword");

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email: user@example.com already in use"));
    }

    @Test
    void shouldLoginSuccessfully_withValidCredentials() throws Exception {
        AuthRequest request = new AuthRequest("user1", "encodedPassword");
        User existingUser = new User();
        existingUser.setUsername("user1");
        existingUser.setEmail("user1@example.com");
        existingUser.setPassword(passwordEncoder.encode("encodedPassword"));
        existingUser.setRole("ROLE_USER");

        userRepository.save(existingUser);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldFailLogin_withInvalidPassword() throws Exception {
        AuthRequest request = new AuthRequest("user", "wrongPassword");


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailLogin_withNonexistentUser() throws Exception {
        AuthRequest request = new AuthRequest("none", "existent");


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());


    }
}