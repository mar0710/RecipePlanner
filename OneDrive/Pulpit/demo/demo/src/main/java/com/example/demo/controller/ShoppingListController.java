package com.example.demo.controller;

import com.example.demo.dtos.IngredientDto;
import com.example.demo.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/shoppinglist")
public class ShoppingListController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getShoppingList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<IngredientDto> ingredientDtos = recipeService.getAllFromPlanner(auth);
        return ResponseEntity.ok(ingredientDtos);

    }
}