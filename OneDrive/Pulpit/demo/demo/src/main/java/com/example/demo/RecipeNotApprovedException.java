package com.example.demo;

public class RecipeNotApprovedException extends RuntimeException{
    public RecipeNotApprovedException(Long id) {
        super("Recipe id:" + id + " not approved");
    }
}
