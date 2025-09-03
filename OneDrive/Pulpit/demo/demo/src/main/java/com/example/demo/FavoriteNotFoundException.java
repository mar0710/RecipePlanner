package com.example.demo;

public class FavoriteNotFoundException extends RuntimeException{
    public FavoriteNotFoundException(Long id) {
        super("Recipe not favorited recipeId:" + id);
    }
}
