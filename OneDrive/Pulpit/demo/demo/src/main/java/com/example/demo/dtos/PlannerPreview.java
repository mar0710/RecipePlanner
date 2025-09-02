package com.example.demo.dtos;

import lombok.Data;
@Data
public class PlannerPreview {
    private Long id;
    private Long recipeId;
    private String imgName;
    private String name;
    private String description;
    private String meal;

    public PlannerPreview() {
    }

    public PlannerPreview(Long id, Long recipeId, String imgName, String name, String description, String meal) {
        this.id = id;
        this.recipeId = recipeId;
        this.imgName = imgName;
        this.name = name;
        this.description = description;
        this.meal = meal;
    }

}