package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private PlannerRepository plannerRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Environment env;

    private User testUser;
    private User adminUser;
    private Recipe testRecipe;
    private Recipe testRecipe2;
    private Recipe testRecipe3;
    private Rating adminRating;
    private Favorite fav;
    private Planner planner;
    @BeforeEach
    void ensureNotProdDb() {
        String url = env.getProperty("spring.datasource.url");
        assertThat(url).contains("h2:mem");
    }
    @BeforeEach
    void setUp() {
        // Clear repositories before each test
        recipeRepository.deleteAll();
        userRepository.deleteAll();
        favoriteRepository.deleteAll();
        plannerRepository.deleteAll();
        ratingRepository.deleteAll();
        ratingRepository.deleteAll();

        // Add a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password"); // hashed if your app hashes it
        testUser.setRole("ROLE_USER");
        testUser = userRepository.save(testUser);

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("password"); // hashed if your app hashes it
        adminUser.setRole("ROLE_ADMIN");
        adminUser = userRepository.save(adminUser);

        testRecipe = new Recipe();
        testRecipe.setPostTime("2022-01-01");
        testRecipe.setName("Test Recipe");
        testRecipe.setImgName("img");
        testRecipe.setDescription("Delicious test recipe");
        testRecipe.setAuthorId(testUser.getId());
        testRecipe.setIsApproved(true);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setAmount("2");
        ingredient1.setProduct("jabłka");
        ingredient1.setRecipe(testRecipe);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setAmount("1");
        ingredient2.setProduct("łyżeczka cynamonu");
        ingredient2.setRecipe(testRecipe);

        testRecipe.setIngredients(Arrays.asList(ingredient1, ingredient2));

        recipeRepository.save(testRecipe);


        testRecipe3 = new Recipe();
        testRecipe3.setPostTime("2022-01-01");
        testRecipe3.setName("Test Recipe");
        testRecipe3.setImgName("img");
        testRecipe3.setDescription("Delicious test recipe");
        testRecipe3.setAuthorId(testUser.getId());
        testRecipe3.setIsApproved(true);
        Ingredient ingredient5 = new Ingredient();
        ingredient5.setAmount("2");
        ingredient5.setProduct("jabłka");
        ingredient5.setRecipe(testRecipe3);

        Ingredient ingredient6 = new Ingredient();
        ingredient6.setAmount("1");
        ingredient6.setProduct("łyżeczka cynamonu");
        ingredient6.setRecipe(testRecipe3);

        testRecipe3.setIngredients(Arrays.asList(ingredient5, ingredient6));

        recipeRepository.save(testRecipe3);

        testRecipe2 = new Recipe();
        testRecipe2.setPostTime("2022-01-05");
        testRecipe2.setName("Test schabowy");
        testRecipe2.setImgName("img1");
        testRecipe2.setDescription("schabowy z zieminakami i kapusta");
        testRecipe2.setAuthorId(testUser.getId());
        testRecipe2.setIsApproved(false);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setAmount("2");
        ingredient3.setProduct("cytryny");
        ingredient3.setRecipe(testRecipe2);

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setAmount("2");
        ingredient4.setProduct("ziemniaki");
        ingredient4.setRecipe(testRecipe2);

        testRecipe2.setIngredients(Arrays.asList(ingredient3, ingredient4));

        recipeRepository.save(testRecipe2);
        fav = new Favorite();
        fav.setUser(testUser);
        fav.setRecipe(testRecipe);
        favoriteRepository.save(fav);

        adminRating = new Rating();
        adminRating.setRecipe(testRecipe);
        adminRating.setUser(adminUser);
        adminRating.setRating(5.0);
        ratingRepository.save(adminRating);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getApprovedRecipes_shouldReturnRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Recipe")))
                .andExpect(jsonPath("$[0].description", is("Delicious test recipe")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getNotApprovedRecipes_shouldReturnNotApprovedRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test schabowy")))
                .andExpect(jsonPath("$[0].description", is("schabowy z zieminakami i kapusta")));
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getNotApprovedRecipes_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/recipes/approve"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access denied"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getNotApprovedRecipeDetails_shouldReturnNotApprovedRecipeDetails() throws Exception {
        mockMvc.perform(get("/api/recipes/approve/{id}",testRecipe2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test schabowy")))
                .andExpect(jsonPath("$.description", is("schabowy z zieminakami i kapusta")))
                .andExpect(jsonPath("$.imgName", is("img1")))
                .andExpect(jsonPath("$.postTime", is("2022-01-05")))
                .andExpect(jsonPath("$.rating", is(0.0)));
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getNotApprovedRecipeDetail_shouldReturn404_whenRecipeNotFound() throws Exception {
        mockMvc.perform(get("/api/recipes/approve/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("Recipe not found id:" + 999L)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getRecipeDetail_shouldReturnRecipeDetails() throws Exception {
        mockMvc.perform(get("/api/recipes/{id}", testRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Recipe")))
                .andExpect(jsonPath("$.description", is("Delicious test recipe")))
                .andExpect(jsonPath("$.authorId", is(testUser.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getRecipeDetail_shouldReturn404_whenRecipeNotFound() throws Exception {
        mockMvc.perform(get("/api/recipes/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("Recipe not found id:" + 999L)));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void searchRecipes_shouldReturnRecipesMatchingKeyword() throws Exception {
        String keyword ="test";
        mockMvc.perform(get("/api/recipes/search/{keyword}", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Recipe")));
    }
    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void isFavorite_shouldReturnTrue() throws Exception {
        mockMvc.perform(get("/api/recipes/{id}/is-favorited", testRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void isFavorite_shouldReturnFalse() throws Exception {
        mockMvc.perform(get("/api/recipes/{id}/is-favorited", testRecipe2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void favorite_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/recipes/{id}/favorite", testRecipe2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Recipe favorite successfully"));
    }
    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unfavorite_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/recipes/{id}/unfavorite", testRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Recipe unfavorite successfully"));
    }
    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unfavorite_shouldReturnNotFavoritedEException() throws Exception {
        mockMvc.perform(delete("/api/recipes/{id}/unfavorite", testRecipe2.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("Recipe not favorited")));

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void approveRecipe_shouldWorkForAdmin() throws Exception {
        mockMvc.perform(put("/api/recipes/approve/{id}",testRecipe2.getId()))
                .andExpect(status().isOk());

        Recipe updated = recipeRepository.findById(testRecipe2.getId()).get();
        assertTrue(updated.isApproved());
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void approveRecipe_shouldReturnForbiddenForNonAdmin() throws Exception {
        mockMvc.perform(put("/api/recipes/approve/{id}", testRecipe2.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getMyRecipes_shouldReturnUserRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes/myrecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Test Recipe"))
                .andExpect(jsonPath("$[0].description").value("Delicious test recipe"));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getMyFavoriteRecipes_shouldReturnFavoritedRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes/favoriterecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Recipe"))
                .andExpect(jsonPath("$[0].description").value("Delicious test recipe"));
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getMyRecipes_shouldReturnEmptyForOtherUser() throws Exception {
        mockMvc.perform(get("/api/recipes/myrecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getMyFavoriteRecipes_shouldReturnEmptyForOtherUser() throws Exception {
        mockMvc.perform(get("/api/recipes/favoriterecipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void rateRecipe_shouldReturn200_whenRatedSuccessfully() throws Exception {
        double ratingValue = 4.5;

        mockMvc.perform(post("/api/recipes/{id}/rate/{rating}", testRecipe.getId(), ratingValue))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe successfully rated"));

        Rating savedRating = ratingRepository.findByUserIdAndRecipeId(testUser.getId(), testRecipe.getId());
        double savedValue = savedRating.getRating();
        assertThat(savedValue).isEqualTo(ratingValue);
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void alreadyRated_shouldReturnRatingIfExists() throws Exception {
        mockMvc.perform(get("/api/recipes/{id}/is-rated", testRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("5.0"));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void alreadyRated_shouldReturnMinusOneIfNotRated() throws Exception {
        mockMvc.perform(get("/api/recipes/{id}/is-rated", testRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("-1.0"));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddRecipeToPlanner() throws Exception {
        Long recipeId = testRecipe.getId();
        String day = "Monday";
        String meal = "Lunch";

        mockMvc.perform(post("/api/recipes/{id}/planner/{day}/{meal}", recipeId, day, meal))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe added successfully"));

        List<Planner> planners = plannerRepository.findAll();
        Planner planner = planners.get(0);

        assertThat(planner.getRecipe().getId()).isEqualTo(recipeId);
        assertThat(planner.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(planner.getDay()).isEqualTo(day);
        assertThat(planner.getMeal()).isEqualTo(meal);
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturnNotFoundWhenRecipeDoesNotExist() throws Exception {
        Long invalidRecipeId = 999L;
        String day = "Tuesday";
        String meal = "Dinner";

        mockMvc.perform(post("/api/recipes/{id}/planner/{day}/{meal}", invalidRecipeId, day, meal))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnForbiddenWhenUserNotLoggedIn() throws Exception {
        Long recipeId = testRecipe.getId();
        String day = "Wednesday";
        String meal = "Breakfast";

        mockMvc.perform(post("/api/recipes/{id}/planner/{day}/{meal}", recipeId, day, meal))
                .andExpect(status().isForbidden());
    }


//    @Test
//    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsServiceImpl", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    void shouldUploadRecipeSuccessfully() throws Exception {
//        recipeRepository.deleteAll();
//        // Given: mock image
//        MockMultipartFile imageFile = new MockMultipartFile(
//                "image",                                 // param name must match controller
//                "test.jpg",                              // filename
//                "image/jpeg",                            // content type
//                "fake image data".getBytes()             // content
//        );
//
//        // Ingredients JSON
//        String ingredientsJson = """
//            [
//                {"amount": "2","product": "Tomato"},
//                {"amount": "100g", "product": "Cheese"}
//            ]
//        """;
//
//        // Perform request
//        mockMvc.perform(multipart("/api/recipes/upload")
//                        .file(imageFile)
//                        .param("name", "Test Recipe")
//                        .param("description", "Tasty test recipe")
//                        .param("ingredients", ingredientsJson)
//                        .with(csrf()))  // required for POST with security
//                .andExpect(status().isOk())
//                .andExpect(content().string("Recipe uploaded successfully!"));
//
//        // Then: recipe should be persisted
//        List<Recipe> recipes = recipeRepository.findAll();
//        assertThat(recipes).hasSize(1);
//        Recipe saved = recipes.get(0);
//        assertThat(saved.getName()).isEqualTo("Test Recipe");
//        assertThat(saved.getDescription()).isEqualTo("Tasty test recipe");
//        assertThat(saved.getImgName()).contains("http://localhost:8080/uploaded-images/");
//        assertThat(saved.getIngredients()).extracting(Ingredient::getProduct)
//                .containsExactlyInAnyOrder("Tomato", "Cheese");
//    }
}

