package com.example.demo.service;

import com.example.demo.*;
import com.example.demo.dtos.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.example.demo.security.CustomUserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlannerRepository plannerRepository;
    @Autowired
    private RatingRepository ratingRepository;

    public RecipePreview convertToPreview(Recipe recipe) {
        RecipePreview dto = new RecipePreview();
        dto.setImgName(recipe.getImgName());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setId(recipe.getId());
        return dto;
    }
    public PlannerPreview convertToPlannerPreview(Planner plan) {
        PlannerPreview dto = new PlannerPreview();
        Recipe recipe = plan.getRecipe();
        dto.setImgName(recipe.getImgName());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setRecipeId(recipe.getId());
        dto.setMeal(plan.getMeal());
        dto.setId(plan.getId());
        return dto;
    }

    public RecipeDetails convertToDetail(Recipe recipe) {
        RecipeDetails dto = new RecipeDetails();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setImgName(recipe.getImgName());
        dto.setRating(recipe.getRating());
        dto.setPostTime(recipe.getPostTime());
        dto.setAuthorId(recipe.getAuthorId());
        List<IngredientDto> ingredientDtos = recipe.getIngredients().stream()
                .map(ingredient -> {
                    IngredientDto ingDto = new IngredientDto();
                    ingDto.setProduct(ingredient.getProduct());
                    ingDto.setAmount(ingredient.getAmount());
                    return ingDto;
                })
                .collect(Collectors.toList());
        dto.setIngredientDtos(ingredientDtos);
        List<CommentDto> commentDtos = recipe.getComments().stream()
                .map(comment -> {
                    CommentDto commDto = new CommentDto();
                    commDto.setId(comment.getId());
                    commDto.setContent(comment.getContent());
                    commDto.setCreated_at(comment.getCreated_at());
                    commDto.setUserName(comment.getUser().getUsername());
                    return commDto;
                })
                .collect(Collectors.toList());
        dto.setCommentDtos(commentDtos);
        return dto;
    }

    public List<RecipePreview> getApprovedRecipes() {
        List<Recipe> recipes = recipeRepository.findByIsApprovedTrue();
        List<RecipePreview> dtos = recipes.stream()
                .map(this::convertToPreview)
                .collect(Collectors.toList());
        return dtos;
    }
    public List<RecipePreview> getNotApprovedRecipes() {
        List<Recipe> recipes = recipeRepository.findByIsApprovedFalse();
        List<RecipePreview> dtos = recipes.stream()
                .map(this::convertToPreview)
                .collect(Collectors.toList());
        return dtos;
    }

    public RecipeDetails getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));

        if (!Boolean.TRUE.equals(recipe.isApproved())) {
            throw new RecipeNotApprovedException(id);
        }

        return convertToDetail(recipe);
    }
    public RecipeDetails getNotApprovedRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return convertToDetail(recipe);
    }
    public void deleteNotApprovedRecipe(Long id){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        recipeRepository.delete(recipe);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
    @Transactional
    public void approveRecipeById(Long id){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe with id " + id + " not found"));

        recipe.setIsApproved(true);
        recipeRepository.save(recipe);
        System.out.println("\n\n\n\n recipe is approvd\n\n\n");
    }

    public List<RecipePreview> searchRecipes(String keyword) {
        List<Recipe> recipes = recipeRepository.searchByKeyword(keyword);
        recipes = recipes.stream().filter(Recipe::isApproved).collect(Collectors.toList());
        return recipes.stream()
                .map(this::convertToPreview)
                .collect(Collectors.toList());
    }

    public List<RecipePreview> getFavoriteRecipes(Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        Optional<List<Favorite>> favs = favoriteRepository.findByUserId(userId);
        List<RecipePreview> dtos = favs
                .orElse(Collections.emptyList())
                .stream()
                .map(fav -> convertToPreview(fav.getRecipe()))
                .collect(Collectors.toList());
        return dtos;
    }
    @Transactional
    public void unfavoriteRecipe(Long recipeId, Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (!favoriteRepository.existsByUserAndRecipe(user, recipe)) {
            throw new FavoriteNotFoundException(recipeId);
        }

        favoriteRepository.deleteByUserAndRecipe(user, recipe);
    }

    public void favoriteRecipe(Long id, Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Favorite fav = new Favorite();
        fav.setRecipe(recipe);
        fav.setUser(user);
        favoriteRepository.save(fav);
    }

    public boolean alreadyFavorite(Long id, Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return favoriteRepository.existsByUserAndRecipe(user, recipe);

    }

    public void addToPlanner(Long id, Authentication auth, String day, String meal) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Planner plan = new Planner();
        plan.setRecipe(recipe);
        plan.setDay(day);
        plan.setMeal(meal);
        plan.setUser(user);
        plannerRepository.save(plan);
    }
    @Transactional
    public void deleteFromPlanner(Long plan_id) {
        Planner planner = plannerRepository.findById(plan_id)
                .orElseThrow(() -> new PlannerNotFoundException(plan_id));
        plannerRepository.delete(planner);
    }

    public List<PlannerPreview> getPlanner(Authentication auth, String day) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Planner> planner = plannerRepository.findByUserAndDay(user, day);
        return planner.stream()
                .map(this::convertToPlannerPreview)
                .collect(Collectors.toList());
    }

    public boolean alreadyRated(Long id, Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        return ratingRepository.existsByUserIdAndRecipeId(userId, id);
    }

    public Double getRating(Long id, Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        Rating rating = ratingRepository.findByUserIdAndRecipeId(userId, id);

        if (rating != null) {
            return rating.getRating();
        } else {
            return null;
        }
    }
    public void rate(Long id, Authentication auth, double rating){
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        Rating rate = ratingRepository.findByUserIdAndRecipeId(userId, id);

        if (rate == null) {
            rate = new Rating();
            rate.setUser(user);
            rate.setRecipe(recipe);
        }

        rate.setRating(rating);
        ratingRepository.save(rate);

    }

    public List<IngredientDto> getAllFromPlanner(Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        List<Planner> plan = plannerRepository.findByUserId(userId);
        return plan.stream()
                .map(Planner::getRecipe)
                .flatMap(recipe -> recipe.getIngredients().stream())
                .map(ingredient -> {
                    IngredientDto ingDto = new IngredientDto();
                    ingDto.setProduct(ingredient.getProduct());
                    ingDto.setAmount(ingredient.getAmount());
                    return ingDto;
                })
                .collect(Collectors.toList());

    }

    public List<RecipePreview> getMyRecipes(Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        List<Recipe> recipes = recipeRepository.findByAuthorId(userId);
        return recipes.stream()
                .map(this::convertToPreview)
                .collect(Collectors.toList());
    }
    private final String uploadDir = "src/main/resources/static/uploaded-images/";
    public void uploadRecipe (String name, String description, MultipartFile imageFile, String ingredientsJson, Authentication auth){
        try {
            File folder = new File(uploadDir);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.write(filePath, imageFile.getBytes());
            Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();

            Recipe recipe = new Recipe();
            recipe.setName(name);
            recipe.setDescription(description);
            recipe.setImgName("http://localhost:8080/uploaded-images/" +  filename);
            recipe.setAuthorId(userId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            recipe.setPostTime(formattedDateTime);

            ObjectMapper objectMapper = new ObjectMapper();
            List<IngredientDto> ingredientDtos = objectMapper.readValue(
                    ingredientsJson,
                    new TypeReference<List<IngredientDto>>() {
                    }
            );

            for (IngredientDto dto : ingredientDtos) {
                Ingredient ingredient = new Ingredient();
                ingredient.setAmount(dto.getAmount());
                ingredient.setProduct(dto.getProduct());
                recipe.addIngredient(ingredient);
            }

            this.saveRecipe(recipe);

        } catch (IOException e) {
            throw new RuntimeException("Error uploading image or parsing ingredients", e);

        }
    }
    public List<String> getHomePhotos(){
        List<Recipe> recipes = recipeRepository.findByIsApprovedTrue();
        List<RecipePreview> dtos = recipes.stream()
                .map(this::convertToPreview)
                .collect(Collectors.toList());
        return dtos.stream()
                .map(RecipePreview::getImgName).limit(4)
                .collect(Collectors.toList());
    }


}