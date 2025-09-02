package com.example.demo.dtos;

import lombok.Data;

@Data
public class IngredientDto {
    private String product;
    private String amount;

    public IngredientDto(String amount,String product) {
        this.product = product;
        this.amount = amount;
    }
    public IngredientDto() {
    }
}