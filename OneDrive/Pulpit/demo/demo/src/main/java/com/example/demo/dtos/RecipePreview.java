package com.example.demo.dtos;

import lombok.Data;

@Data
public class RecipePreview{
    private Long id;
    private String imgName;
    private String name;
    private String description;

    public RecipePreview(long id, String imageName, String name, String description) {
        this.id=id;
        this.imgName = imageName;
        this.name = name;
        this.description = description;
    }

    public RecipePreview() {
    }
}