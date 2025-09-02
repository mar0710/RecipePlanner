package com.example.demo.dtos;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDetails {
    private Long id;
    private String name;
    private String description;
    private String imgName;
    private double rating;
    private String postTime;
    private Long authorId;
    private List<IngredientDto> ingredientDtos;
    private List<CommentDto> commentDtos;
}