<?php

namespace models;

class Ingredient
{
    private int $recipe_id;
    private string $amount;
    private string $product;
    public function __construct(int $recipe_id, string $amount, string $product){
        $this->recipe_id = $recipe_id;
        $this->amount = $amount;
        $this->product = $product;
    }
    public function getRecipeId(): int
    {
        return $this->recipe_id;
    }

    public function setRecipeId(int $recipe_id): void
    {
        $this->recipe_id = $recipe_id;
    }

    public function getAmount(): string
    {
        return $this->amount;
    }

    public function setAmount(string $amount): void
    {
        $this->amount = $amount;
    }

    public function getProduct(): string
    {
        return $this->product;
    }

    public function setProduct(string $product): void
    {
        $this->product = $product;
    }



}