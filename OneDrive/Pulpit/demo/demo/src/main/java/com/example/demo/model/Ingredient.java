package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name="ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;
    private String amount;

    private Long recipeId;

    public Ingredient() {}

    public Ingredient(String product, String amount, Long recipeId) {
        this.product = product;
        this.amount = amount;
        this.recipeId = recipeId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
    public Long getRecipe() { return recipeId; }
    public void setRecipe(Long recipeId) { this.recipeId = recipeId; }
}
