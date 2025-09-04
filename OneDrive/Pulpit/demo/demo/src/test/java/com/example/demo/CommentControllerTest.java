package com.example.demo;
import com.example.demo.controller.CommentController;
import com.example.demo.dtos.RecipeDetails;
import com.example.demo.dtos.RecipePreview;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.CommentService;
import com.example.demo.service.RecipeService;
import com.example.demo.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

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

@AutoConfigureMockMvc(addFilters = true)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private CommentService commentService;

    @Test
    void addComment_shouldReturn200_andComment() throws Exception {
        Long recipeId = 1L;
        String content = "Delicious recipe!";

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        doNothing().when(commentService).addComment(recipeId, auth, content);

        // Perform request and assert
        mockMvc.perform(post("/api/recipes/{id}/comment", recipeId)
                        .param("content", content))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment added successfully!"));

        // Verify service was called
        verify(commentService).addComment(recipeId, auth, content);
    }

    @Test
    void addComment_shouldReturn500_whenServiceThrowsException() throws Exception {
        Long recipeId = 1L;
        String content = "Delicious recipe!";

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(commentService.addComment(recipeId, auth, content))
                .thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(post("/api/recipes/{id}/comment", recipeId)
                        .param("content", content))
                .andExpect(status().isInternalServerError());

        verify(commentService).addComment(recipeId, auth, content);
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void deleteComment_shouldReturn200_whenDeleted() throws Exception {
        Long commentId = 5L;
        Long id = 1L;
        doNothing().when(commentService).deleteById(commentId);

        mockMvc.perform(delete("/api/recipes/{id}/deletecomment/{commentId}", id, commentId))
                .andExpect(status().isOk());

        verify(commentService).deleteById(commentId);
    }

}

