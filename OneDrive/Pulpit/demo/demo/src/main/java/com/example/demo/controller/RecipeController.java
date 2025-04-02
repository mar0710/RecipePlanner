package com.example.demo.controller;

import com.example.demo.RecipeNotFoundException;
import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class RecipeController {

    private final RecipeRepository repository;

    RecipeController(RecipeRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/recipes")
    List<Recipe> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/recipes")
    Recipe newRecipe(@RequestBody Recipe newRecipe) {
        return repository.save(newRecipe);
    }

    // Single item

    @GetMapping("/recipes/{id}")
    Recipe one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @PutMapping("/recipes/{id}")
    Recipe replaceRecipe(@RequestBody Recipe newRecipe, @PathVariable Long id) {

        return repository.findById(id)
                .map(recipe -> {
                    recipe.setName(newRecipe.getName());
                    return repository.save(recipe);
                })
                .orElseGet(() -> {
                    return repository.save(newRecipe);
                });
    }

    @DeleteMapping("/recipes/{id}")
    void deleteRecipe(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
