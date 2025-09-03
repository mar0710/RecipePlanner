package com.example.demo;

public class RecipeNotFoundException extends RuntimeException{
    public RecipeNotFoundException(Long id) {
        super("Recipe not found id:" + id);
    }
}
