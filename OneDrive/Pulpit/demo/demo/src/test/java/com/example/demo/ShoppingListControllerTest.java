package com.example.demo;
import com.example.demo.controller.RecipeController;
import com.example.demo.controller.ShoppingListController;
import com.example.demo.dtos.IngredientDto;
import com.example.demo.dtos.RecipePreview;
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

import java.util.List;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ShoppingListController.class)
class ShoppingListControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void getShoppingList_shouldReturn200_andIngredientList() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Prepare dummy data
        List<IngredientDto> dummyIngredients = List.of(
                new IngredientDto("2", "Tomato"),
                new IngredientDto("1", "Onion")
        );

        // Stub service method
        when(recipeService.getAllFromPlanner(auth)).thenReturn(dummyIngredients);

        // Perform GET request
        mockMvc.perform(get("/api/shoppinglist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(dummyIngredients.size()))
                .andExpect(jsonPath("$[0].amount").value("2"))
                .andExpect(jsonPath("$[0].product").value("Tomato"))
                .andExpect(jsonPath("$[1].amount").value("1"))
                .andExpect(jsonPath("$[1].product").value("Onion"));

        // Verify service was called
        verify(recipeService).getAllFromPlanner(auth);
    }
}