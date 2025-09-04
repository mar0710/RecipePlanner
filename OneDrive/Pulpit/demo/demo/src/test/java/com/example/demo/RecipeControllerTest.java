package com.example.demo;
import com.example.demo.controller.RecipeController;
import com.example.demo.dtos.RecipeDetails;
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
@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void shouldReturnListOfRecipePreviews() throws Exception {
        RecipePreview preview1 = new RecipePreview(1L, "img1.jpg", "Makaron", "Szybki obiad");
        RecipePreview preview2 = new RecipePreview(2L, "img2.jpg", "Ciasto", "Na deser");

        List<RecipePreview> previews = List.of(preview1, preview2);
        when(recipeService.getApprovedRecipes()).thenReturn(previews);

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].imgName").value("img1.jpg"))
                .andExpect(jsonPath("$[0].name").value("Makaron"))
                .andExpect(jsonPath("$[0].description").value("Szybki obiad"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].imgName").value("img2.jpg"))
                .andExpect(jsonPath("$[1].name").value("Ciasto"))
                .andExpect(jsonPath("$[1].description").value("Na deser"));
    }
    @Test
    void testGetApprovedRecipes_NotFound() throws Exception {
        Mockito.when(recipeService.getApprovedRecipes()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // expect 404
    }
    @Test
    void shouldReturnListOfNotApprovedRecipePreviews() throws Exception {
        RecipePreview preview1 = new RecipePreview(1L, "img1.jpg", "Makaron", "Szybki obiad");
        RecipePreview preview2 = new RecipePreview(2L, "img2.jpg", "Ciasto", "Na deser");

        List<RecipePreview> previews = List.of(preview1, preview2);
        when(recipeService.getNotApprovedRecipes()).thenReturn(previews);

        mockMvc.perform(get("/api/recipes/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].imgName").value("img1.jpg"))
                .andExpect(jsonPath("$[0].name").value("Makaron"))
                .andExpect(jsonPath("$[0].description").value("Szybki obiad"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].imgName").value("img2.jpg"))
                .andExpect(jsonPath("$[1].name").value("Ciasto"))
                .andExpect(jsonPath("$[1].description").value("Na deser"));
    }
    @Test
    void testGetNotApprovedRecipes_NotFound() throws Exception {
        Mockito.when(recipeService.getNotApprovedRecipes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/recipes/approve")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetNotApprovedRecipeDetail_OK() throws Exception {
        Long id = 1L;

        RecipeDetails details = new RecipeDetails();
        details.setId(id);
        details.setName("Pasta");
        details.setDescription("Delicious pasta");
        details.setImgName("pasta.jpg");
        details.setRating(4.5);
        details.setAuthorId(2L);
        details.setPostTime("2025-08-28");
        details.setIngredientDtos(Collections.emptyList());
        details.setCommentDtos(Collections.emptyList());

        Mockito.when(recipeService.getNotApprovedRecipeById(id)).thenReturn(details);

        mockMvc.perform(get("/api/recipes/approve/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Pasta"))
                .andExpect(jsonPath("$.description").value("Delicious pasta"))
                .andExpect(jsonPath("$.imgName").value("pasta.jpg"))
                .andExpect(jsonPath("$.rating").value(4.5))
                .andExpect(jsonPath("$.authorId").value(2))
                .andExpect(jsonPath("$.postTime").value("2025-08-28"))
                .andExpect(jsonPath("$.ingredientDtos").isArray())
                .andExpect(jsonPath("$.commentDtos").isArray());
    }
    @Test
    void testGetNotApprovedRecipeDetail_NotFound() throws Exception {
        Long recipeId = 99L;

        when(recipeService.getNotApprovedRecipeById(recipeId))
                .thenThrow(new RecipeNotFoundException(recipeId));

        mockMvc.perform(get("/api/recipes/approve/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetRecipeDetail_OK() throws Exception {
        Long id = 1L;

        RecipeDetails details = new RecipeDetails();
        details.setId(id);
        details.setName("Pasta");
        details.setDescription("Delicious pasta");
        details.setImgName("pasta.jpg");
        details.setRating(4.5);
        details.setAuthorId(2L);
        details.setPostTime("2025-08-28");
        details.setIngredientDtos(Collections.emptyList());
        details.setCommentDtos(Collections.emptyList());

        Mockito.when(recipeService.getRecipeById(id)).thenReturn(details);

        mockMvc.perform(get("/api/recipes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Pasta"))
                .andExpect(jsonPath("$.description").value("Delicious pasta"))
                .andExpect(jsonPath("$.imgName").value("pasta.jpg"))
                .andExpect(jsonPath("$.rating").value(4.5))
                .andExpect(jsonPath("$.authorId").value(2))
                .andExpect(jsonPath("$.postTime").value("2025-08-28"))
                .andExpect(jsonPath("$.ingredientDtos").isArray())
                .andExpect(jsonPath("$.commentDtos").isArray());
    }

    @Test
    void testGetRecipeDetail_NotFound() throws Exception {
        Long recipeId = 99L;

        // Mock service to throw exception
        when(recipeService.getRecipeById(recipeId))
                .thenThrow(new RecipeNotFoundException(recipeId));

        mockMvc.perform(get("/api/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetRecipeDetail_Forbidden() throws Exception {
        Long recipeId = 99L;

        when(recipeService.getRecipeById(recipeId))
                .thenThrow(new RecipeNotApprovedException(recipeId));

        mockMvc.perform(get("/api/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void isFavorited_shouldReturnTrue_whenRecipeIsFavorited() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(recipeService.alreadyFavorite(1L, auth)).thenReturn(true);
        mockMvc.perform(get("/api/recipes/1/is-favorited"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
    @Test
    void isFavorited_shouldReturnFalse_whenRecipeIsNotFavorited() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(recipeService.alreadyFavorite(1L, auth)).thenReturn(false);
        mockMvc.perform(get("/api/recipes/1/is-favorited"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }



    @Test
    void favorite_shouldReturn200_whenFavorited() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/api/recipes/1/favorite"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe favorite successfully"));

        verify(recipeService).favoriteRecipe(eq(1L), same(auth));
    }

    @Test
    void favorite_shouldReturn404_whenRecipeNotFound() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        doThrow(new RecipeNotFoundException(42L))
                .when(recipeService).favoriteRecipe(eq(42L), same(auth));

        mockMvc.perform(post("/api/recipes/42/favorite"))
                .andExpect(status().isNotFound());
    }

    @Test
    void unfavorite_shouldReturn200_whenUnfavorited() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        mockMvc.perform(delete("/api/recipes/1/unfavorite"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe unfavorite successfully"));

        verify(recipeService).unfavoriteRecipe(eq(1L), same(auth));
    }

    @Test
    void unfavorite_shouldReturn404_whenFavoriteNotFound() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        doThrow(new FavoriteNotFoundException(77L))
                .when(recipeService).unfavoriteRecipe(eq(77L), same(auth));

        mockMvc.perform(delete("/api/recipes/77/unfavorite"))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadRecipe_shouldReturn200_whenUploadSucceeds() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "pasta.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );

        String ingredientsJson = "[{\"amount\":\"2\",\"product\":\"Tomato\"}]";
        doNothing().when(recipeService).uploadRecipe(
                anyString(),
                anyString(),
                any(MultipartFile.class),
                anyString(),
                any(Authentication.class)
        );

        mockMvc.perform(multipart("/api/recipes/upload")
                        .file(image)
                        .param("name", "Pasta")
                        .param("description", "Delicious pasta")
                        .param("ingredients", ingredientsJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe uploaded successfully!"));

        verify(recipeService).uploadRecipe(
                eq("Pasta"),
                eq("Delicious pasta"),
                any(MultipartFile.class),
                eq(ingredientsJson),
                eq(auth)
        );
    }

    @Test
    void uploadRecipe_shouldReturn500_whenServiceThrowsException() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "bad.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "bad".getBytes()
        );

        String ingredientsJson = "[{\"amount\":\"2\",\"product\":\"Tomato\"}]";

        doThrow(new RuntimeException("Error uploading image or parsing ingredients"))
                .when(recipeService)
                .uploadRecipe(
                        anyString(),
                        anyString(),
                        any(MultipartFile.class),
                        anyString(),
                        eq(auth)
                );
        mockMvc.perform(multipart("/api/recipes/upload")
                        .file(image)
                        .param("name", "Bad recipe")
                        .param("description", "Should fail")
                        .param("ingredients", ingredientsJson))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void approveRecipe_shouldCallService_whenIdProvided() throws Exception {
        Long recipeId = 1L;

        // doNothing is default for void methods, but we can be explicit
        doNothing().when(recipeService).approveRecipeById(recipeId);

        mockMvc.perform(put("/api/recipes/approve/{id}", recipeId))
                .andExpect(status().isOk()); // 200 OK by default for void endpoint

        verify(recipeService).approveRecipeById(recipeId);
    }

    @Test
    void delete_shouldReturn200_whenRecipeDeleted() throws Exception {
        Long recipeId = 1L;

        doNothing().when(recipeService).deleteNotApprovedRecipe(recipeId);

        mockMvc.perform(delete("/api/recipes/approve/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe deleted successfully"));

        verify(recipeService).deleteNotApprovedRecipe(recipeId);
    }


    @Test
    void searchRecipes_shouldReturnList_whenKeywordProvided() throws Exception {
        String keyword = "pasta";

        // Prepare a mock result
        RecipePreview preview = new RecipePreview();
        preview.setId(1L);
        preview.setImgName("img");
        preview.setName("Pasta");
        preview.setDescription("Delicious pasta");

        RecipePreview preview2 = new RecipePreview();
        preview2.setId(2L);
        preview2.setImgName("img1");
        preview2.setName("spaghetti");
        preview2.setDescription("pasta for dinner");
        List<RecipePreview> mockResults = List.of(preview, preview2);

        when(recipeService.searchRecipes(keyword)).thenReturn(mockResults);

        mockMvc.perform(get("/api/recipes/search/{keyword}", keyword)
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].imgName").value("img"))
                .andExpect(jsonPath("$[0].name").value("Pasta"))
                .andExpect(jsonPath("$[0].description").value("Delicious pasta"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].imgName").value("img1"))
                .andExpect(jsonPath("$[1].name").value("spaghetti"))
                .andExpect(jsonPath("$[1].description").value("pasta for dinner"));

        verify(recipeService).searchRecipes(keyword);
    }

    @Test
    void searchRecipes_shouldReturnEmptyList_whenNoMatches() throws Exception {
        String keyword = "xyz";

        when(recipeService.searchRecipes(keyword)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/recipes/search/{keyword}", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(recipeService).searchRecipes(keyword);
    }
    @Test
    void searchRecipes_shouldReturnEmptyList_whenNoRecipesFound() throws Exception {
        String keyword = "xyz";

        when(recipeService.searchRecipes(keyword)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/recipes/search/{keyword}", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(recipeService).searchRecipes(keyword);
    }

    @Test
    void addToPlanner_shouldReturn200_whenSuccess() throws Exception {
        Long recipeId = 1L;
        String day = "Monday";
        String meal = "Lunch";

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // doNothing for void method
        doNothing().when(recipeService).addToPlanner(recipeId, auth, day, meal);

        mockMvc.perform(post("/api/recipes/{id}/planner/{day}/{meal}", recipeId, day, meal))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe deleted successfully"));

        verify(recipeService).addToPlanner(recipeId, auth, day, meal);
    }

    @Test
    void addToPlanner_shouldReturn500_whenServiceThrowsException() throws Exception {
        Long recipeId = 1L;
        String day = "Monday";
        String meal = "Lunch";

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        doThrow(new RuntimeException("Planner service failure"))
                .when(recipeService).addToPlanner(recipeId, auth, day, meal);

        mockMvc.perform(post("/api/recipes/{id}/planner/{day}/{meal}", recipeId, day, meal))
                .andExpect(status().isInternalServerError());

        verify(recipeService).addToPlanner(recipeId, auth, day, meal);
    }

    @Test
    void rateRecipe_shouldReturn200_whenSuccessful() throws Exception {
        Long recipeId = 1L;
        double ratingValue = 4.5;

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // doNothing because rate is a void method
        doNothing().when(recipeService).rate(recipeId, auth, ratingValue);

        mockMvc.perform(post("/api/recipes/{id}/rate/{rating}", recipeId, ratingValue))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe successfully rated"));

        verify(recipeService).rate(recipeId, auth, ratingValue);
    }

    @Test
    void alreadyRated_shouldReturnRating_whenRated() throws Exception {
        Long recipeId = 1L;
        double ratingValue = 4.5;

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(recipeService.getRating(recipeId, auth)).thenReturn(ratingValue);

        mockMvc.perform(get("/api/recipes/{id}/is-rated", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(ratingValue)));

        verify(recipeService).getRating(recipeId, auth);
    }

    @Test
    void alreadyRated_shouldReturnMinusOne_whenNotRated() throws Exception {
        Long recipeId = 1L;

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(recipeService.getRating(recipeId, auth)).thenReturn(null);

        mockMvc.perform(get("/api/recipes/{id}/is-rated", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string("-1.0"));

        verify(recipeService).getRating(recipeId, auth);
    }

    @Test
    void getMyRecipes_shouldReturnList() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        RecipePreview preview = new RecipePreview();
        preview.setId(1L);
        preview.setName("Pasta");
        preview.setDescription("Delicious pasta");

        when(recipeService.getMyRecipes(auth)).thenReturn(Collections.singletonList(preview));

        mockMvc.perform(get("/api/recipes/myrecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pasta"))
                .andExpect(jsonPath("$[0].description").value("Delicious pasta"));

        verify(recipeService).getMyRecipes(auth);
    }

    @Test
    void getMyFavoriteRecipes_shouldReturnList() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        RecipePreview preview = new RecipePreview();
        preview.setId(2L);
        preview.setName("Salad");
        preview.setDescription("Healthy salad");

        when(recipeService.getFavoriteRecipes(auth)).thenReturn(Collections.singletonList(preview));

        mockMvc.perform(get("/api/recipes/favoriterecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Salad"))
                .andExpect(jsonPath("$[0].description").value("Healthy salad"));

        verify(recipeService).getFavoriteRecipes(auth);
    }

    @Test
    void getMyRecipes_shouldReturnEmptyList_whenNone() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(recipeService.getMyRecipes(auth)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/recipes/myrecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(recipeService).getMyRecipes(auth);
    }

    @Test
    void getMyFavoriteRecipes_shouldReturnEmptyList_whenNone() throws Exception {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(recipeService.getFavoriteRecipes(auth)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/recipes/favoriterecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(recipeService).getFavoriteRecipes(auth);
    }
    @Test
    void getHomePhotos_shouldReturnList() throws Exception{
        String s1="owsianka.jpg";
        String s2="kanapka.jpg";
        List<String> photos = List.of(s1,s2);
        when(recipeService.getHomePhotos()).thenReturn(photos);
        mockMvc.perform(get("/api/recipes/homephotos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("owsianka.jpg"))
                .andExpect(jsonPath("$[1]").value("kanapka.jpg"));

    }

}