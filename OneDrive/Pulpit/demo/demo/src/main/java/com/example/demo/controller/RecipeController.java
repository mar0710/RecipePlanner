package com.example.demo.controller;
import com.example.demo.dtos.RecipeDetails;
import com.example.demo.dtos.RecipePreview;
import com.example.demo.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipePreview>> getApprovedRecipes() {
        List<RecipePreview> recipes = recipeService.getApprovedRecipes();
        if (recipes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipes);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/approve")
    public ResponseEntity<List<RecipePreview>> getNotApprovedRecipes() {
        List<RecipePreview> recipes = recipeService.getNotApprovedRecipes();
        if (recipes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipes);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/approve/{id}")
    public ResponseEntity<RecipeDetails> getNotApprovedRecipeDetail(@PathVariable Long id) {
        RecipeDetails details = recipeService.getNotApprovedRecipeById(id);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetails> getRecipeDetail(@PathVariable Long id) {
        RecipeDetails details = recipeService.getRecipeById(id);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}/is-favorited")
    public ResponseEntity<Boolean> isFavorited(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(recipeService.alreadyFavorite(id, auth));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<String> favorite(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        recipeService.favoriteRecipe(id, auth);
        return ResponseEntity.ok("Recipe favorite successfully");
    }

    @DeleteMapping("/{id}/unfavorite")
    public ResponseEntity<String> unfavorite(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        recipeService.unfavoriteRecipe(id, auth);
        return ResponseEntity.ok("Recipe unfavorite successfully");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadRecipe(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("image") MultipartFile imageFile, @RequestParam("ingredients") String ingredientsJson) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            recipeService.uploadRecipe( name, description, imageFile, ingredientsJson, auth);
            return ResponseEntity.ok("Recipe uploaded successfully!");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("approve/{id}")
    public void approveRecipe(@PathVariable Long id){
        recipeService.approveRecipeById(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/approve/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        recipeService.deleteNotApprovedRecipe(id);
        return ResponseEntity.ok("Recipe deleted successfully");
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<RecipePreview>> searchRecipes(@PathVariable String keyword) {
        List<RecipePreview> recipes = recipeService.searchRecipes(keyword);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/{id}/planner/{day}/{meal}")
    public ResponseEntity<String> addToPlanner(@PathVariable Long id, @PathVariable String day, @PathVariable String meal ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        recipeService.addToPlanner(id, auth, day, meal);
        return ResponseEntity.ok("Recipe added successfully");

    }
    @PostMapping("/{id}/rate/{rating}")
    public ResponseEntity<String> rateRecipe(@PathVariable Long id, @PathVariable double rating){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        recipeService.rate(id, auth, rating);
        return ResponseEntity.ok("Recipe successfully rated");
    }
    @GetMapping("/{id}/is-rated")
    public ResponseEntity<Double> AlreadyRated(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Double rating = recipeService.getRating(id, auth);

        if (rating != null) {
            return ResponseEntity.ok(rating);
        } else {
            return ResponseEntity.ok(-1.0);
        }
    }

    @GetMapping("/myrecipes")
    public ResponseEntity<List<RecipePreview>> getMyRecipes(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(recipeService.getMyRecipes(auth));
    }
    @GetMapping("/favoriterecipes")
    public ResponseEntity<List<RecipePreview>> getMyFavoriteRecipes(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(recipeService.getFavoriteRecipes(auth));
    }
    @GetMapping("homephotos")
    public ResponseEntity<List<String>> getHomePagePhotos() {
        return ResponseEntity.ok(recipeService.getHomePhotos());
    }



}