package com.example.demo;
import com.example.demo.controller.PlannerController;
import com.example.demo.dtos.PlannerPreview;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.RecipeService;
import com.example.demo.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PlannerController.class)
class PlannerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void planner_shouldReturn200_andPlannerList() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        List<PlannerPreview> dummyPlanner = List.of(
                new PlannerPreview(1L, 1L,"img1.jpg","Pasta", "pasta for lunch", "lunch"),
                new PlannerPreview(2L, 2L,"img2.jpg","Salad", "salad for lunch", "lunch")
        );
        
        when(recipeService.getPlanner(auth, "Monday")).thenReturn(dummyPlanner);
        
        mockMvc.perform(get("/api/planner/Monday"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(dummyPlanner.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].recipeId").value(1L))
                .andExpect(jsonPath("$[0].imgName").value("img1.jpg"))
                .andExpect(jsonPath("$[0].name").value("Pasta"))
                .andExpect(jsonPath("$[0].description").value("pasta for lunch"))
                .andExpect(jsonPath("$[0].meal").value("lunch"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].recipeId").value(2L))
                .andExpect(jsonPath("$[1].imgName").value("img2.jpg"))
                .andExpect(jsonPath("$[1].name").value("Salad"))
                .andExpect(jsonPath("$[1].description").value("salad for lunch"))
                .andExpect(jsonPath("$[1].meal").value("lunch"));

        verify(recipeService).getPlanner(auth, "Monday");
    }


    @Test
    void deleteFromPlanner_shouldReturn200_andSuccessMessage() throws Exception {
        Long recipeId = 1L;
        
        doNothing().when(recipeService).deleteFromPlanner(recipeId);
        
        mockMvc.perform(delete("/api/planner/delete/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe deleted successfully from planner"));

        verify(recipeService).deleteFromPlanner(recipeId);
    }

    @Test
    void deleteFromPlanner_shouldReturn404_whenPlannerNotFound() throws Exception {
        Long invalidId = 999L;
        
        doThrow(new PlannerNotFoundException(invalidId))
                .when(recipeService).deleteFromPlanner(invalidId);
        
        mockMvc.perform(delete("/api/planner/delete/{id}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Planner not found for: " + invalidId));

        verify(recipeService).deleteFromPlanner(invalidId);
    }

}
