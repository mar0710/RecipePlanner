package com.example.demo;
import com.example.demo.controller.AuthController;
import com.example.demo.dtos.*;
import com.example.demo.model.Favorite;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Recipe;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.demo.controller.RecipeController;
import com.example.demo.controller.PlannerController;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.RecipeService;
import com.example.demo.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();




    @Test
    void login_shouldReturn200_andToken_whenAuthenticationSucceeds() throws Exception {
        AuthRequest request = new AuthRequest("john", "password123");
        AuthResponse expectedResponse = new AuthResponse("fake-jwt-token");

        when(authService.authenticate(any(AuthRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authService).authenticate(any(AuthRequest.class));
    }


    @Test
    void registerUser_shouldReturn200_whenRegistrationSucceeds() throws Exception {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "password123", "ROLE_USER");
        MessageResponse expectedResponse = new MessageResponse("User registered successfully!");

        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void registerUser_shouldReturn400_whenUsernameAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "password123", "ROLE_USER");
        MessageResponse expectedResponse = new MessageResponse("Error: Username is already taken!");

        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void registerUser_shouldReturn400_whenEmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest("jane", "jane@example.com", "password123", "ROLE_USER");
        MessageResponse expectedResponse = new MessageResponse("Error: Email is already taken!");

        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

        verify(authService).register(any(RegisterRequest.class));
    }

}