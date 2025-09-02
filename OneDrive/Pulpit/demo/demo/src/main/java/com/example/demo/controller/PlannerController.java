package com.example.demo.controller;

import com.example.demo.dtos.PlannerPreview;
import com.example.demo.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/planner")
public class PlannerController {
    @Autowired
    RecipeService recipeService;
    @GetMapping("/{day}")
    public ResponseEntity<List<PlannerPreview>> planner(@PathVariable String day) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<PlannerPreview> planner = recipeService.getPlanner(auth, day);
        return ResponseEntity.ok(planner);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFromPlanner(@PathVariable Long id) {
        recipeService.deleteFromPlanner(id);
        return ResponseEntity.ok("Recipe deleted successfully from planner");

    }
}
