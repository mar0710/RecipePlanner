<?php

use models\Ingredient;
use models\Recipe;

require_once 'Repository.php';
require_once __DIR__ . '/../models/Recipe.php';

class RecipeRepository extends Repository
{

    public function getRecipe(int $id): ?Recipe
    {
        $stmt = $this->database->connect()->prepare('
            SELECT * FROM public.recipes WHERE id = :id
        ');
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);
        $stmt->execute();

        $recipe = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($recipe == false) {
            return null;
        }

        return new Recipe(
            $recipe['name'],
            $recipe['description'],
            $recipe['image']
        );
    }

    public function addRecipe(Recipe $recipe):int
    {
        $date = new DateTime();
        $stmt = $this->database->connect()->prepare('
            INSERT INTO recipes (name , description, img_name, created_at, author_id)
            VALUES (?, ?, ?, ?, ?) RETURNING id;
        ');


        $stmt->execute([
            $recipe->getName(),
            $recipe->getDescription(),
            $recipe->getImage(),
            $date->format('Y-m-d'),
            $recipe->getUserId()
        ]);
        return $stmt->fetchColumn();
    }
    public function addIngredient(Ingredient $ingredient): void{
        $stmt = $this->database->connect()->prepare('
            INSERT INTO ingredients (recipe_id, amount, product)
            VALUES (?, ?, ?)
        ');
        $stmt->execute([
            $ingredient->getRecipeId(),
            $ingredient->getAmount(),
            $ingredient->getProduct(),
        ]);
    }
    public function getRecipes(): array
    {
        $result = [];

        $stmt = $this->database->connect()->prepare('
            SELECT * FROM recipes;
        ');
        $stmt->execute();
        $recipes = $stmt->fetchAll(PDO::FETCH_ASSOC);

        foreach ($recipes as $recipe) {
            $result[] = new Recipe(
                $recipe['id'],
                $recipe['name'],
                $recipe['description'],
                $recipe['image'],

            );
        }

        return $result;
    }

    public function getRecipeByName(string $searchString)
    {
        $searchString = '%' . strtolower($searchString) . '%';

        $stmt = $this->database->connect()->prepare('
            SELECT * FROM recipes WHERE LOWER(name) LIKE :search OR LOWER(description) LIKE :search
        ');
        $stmt->bindParam(':search', $searchString, PDO::PARAM_STR);
        $stmt->execute();

        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
}