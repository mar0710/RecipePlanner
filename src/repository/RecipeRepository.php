<?php

use models\Comment;
use models\Ingredient;
use models\Recipe;

require_once 'Repository.php';
require_once __DIR__ . '/../models/Recipe.php';
require_once __DIR__ . '/../models/Ingredient.php';
require_once __DIR__ . '/../models/Comment.php';

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

        $result = new Recipe(
            $recipe['author_id'],
            $recipe['name'],
            $recipe['description'],
            $recipe['img_name'],
            $recipe['id']
        );
        $result->setRating($recipe['rating']);
        return $result;
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
    public function getIngredients(int $recipeId): array
    {
        $results = [];
        $stmt = $this->database->connect()->prepare('
            SELECT * FROM ingredients WHERE recipe_id = :recipeId
        ');

        $stmt->bindParam(':recipeId', $recipeId, PDO::PARAM_INT);
        $stmt->execute();
        $ingredients = $stmt->fetchAll(PDO::FETCH_ASSOC);
        foreach ($ingredients as $ingredient) {
            $result= new Ingredient(
                $ingredient['recipe_id'],
                $ingredient['amount'],
                $ingredient['product']
        );
            $results[]=$result;
        }
        return $results;

    }
    public function getRecipes(): array
    {
        $results = [];

        $stmt = $this->database->connect()->prepare('
            SELECT * FROM recipes;
        ');
        $stmt->execute();
        $recipes = $stmt->fetchAll(PDO::FETCH_ASSOC);

        foreach ($recipes as $recipe) {
            $result = new Recipe(
                $recipe['author_id'],
                $recipe['name'],
                $recipe['description'],
                $recipe['img_name'],
                $recipe['id']
            );
            $results[] = $result;
        }

        return $results;
    }

    public function getRecipeByName(string $searchString)
    {
        $searchString = '%' . strtolower($searchString) . '%';

        $stmt = $this->database->connect()->prepare('
            SELECT * FROM recipes WHERE LOWER(name) LIKE :search OR LOWER(description) LIKE :search
        ');
        $stmt->bindParam(':search', $searchString, PDO::PARAM_STR);
        $stmt->execute();
        $recipes=$stmt->fetchAll(PDO::FETCH_ASSOC);
        $results = [];
        foreach ($recipes as $recipe) {
            $result = new Recipe(
                $recipe['author_id'],
                $recipe['name'],
                $recipe['description'],
                $recipe['img_name'],
                $recipe['id']
            );
            $results[] = $result;
        }
        return $results;
    }
    public function myRecipes($userId): array{
        $result = [];
        $stmt = $this->database->connect()->prepare('
            SELECT * FROM recipes WHERE author_id = :userId
        ');
        $stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
        $stmt->execute();
        $recipes = $stmt->fetchAll(PDO::FETCH_ASSOC);

        foreach ($recipes as $recipe) {
            $result[] = new Recipe(
                $recipe['author_id'],
                $recipe['name'],
                $recipe['description'],
                $recipe['img_name'],
                $recipe['id']
            );
        }
        return $result;
    }
    public function getComments(int $recipeId): array
    {
        $results = [];
        $stmt = $this->database->connect()->prepare('
            SELECT * FROM comments c JOIN users u on c.user_id= u.id WHERE recipe_id = :recipeId
        ');

        $stmt->bindParam(':recipeId', $recipeId, PDO::PARAM_INT);
        $stmt->execute();
        $comments = $stmt->fetchAll(PDO::FETCH_ASSOC);
        foreach ($comments as $comment) {
            $result= new Comment(
                $comment['recipe_id'],
                $comment['content'],
                $comment['user_id'],
            );
            $result->setUserName($comment['username']);
            $result->setDate($comment['created_at']);
            $results[]=$result;
        }
        return $results;

    }
    public function addComment(Comment $comment)
    {
        $date = new DateTime();
        $stmt = $this->database->connect()->prepare('
            INSERT INTO comments (recipe_id, content, user_id, created_at)
            VALUES (?, ?, ?, ?)
        ');
        $stmt->execute([
            $comment->getRecipeId(),
            $comment->getComment(),
            $comment->getAuthorId(),
            $date->format('Y-m-d'),
        ]);

    }
    public function myFavorites($userId): array{
        $results = [];
        $stmt = $this->database->connect()->prepare('
            SELECT * FROM recipes r JOIN favorites f on r.id=f.recipe_id WHERE f.user_id = :userId
        ');
        $stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
        $stmt->execute();
        $recipes = $stmt->fetchAll(PDO::FETCH_ASSOC);
        foreach ($recipes as $recipe) {
            $result = new Recipe(
                $recipe['author_id'],
                $recipe['name'],
                $recipe['description'],
                $recipe['img_name'],
                $recipe['recipe_id']
            );
            $results[] = $result;
        }
        return $results;
    }
    public function addToFavorites($userId, $recipeId)
    {
        if($this->checkFavorite($recipeId, $userId)==0) {
            $stmt = $this->database->connect()->prepare('
                INSERT INTO favorites (user_id, recipe_id)
                VALUES (?, ?)
            ');
            $stmt->execute([
                $userId,
                $recipeId
            ]);
        }
    }
    private function checkFavorite($recipeId, $userId): int
    {
        $stmt = $this->database->connect()->prepare('
            SELECT COUNT(*) FROM favorites WHERE user_id = :userId AND recipe_id = :recipeId
        ');
        $stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
        $stmt->bindParam(':recipeId', $recipeId, PDO::PARAM_INT);
        $stmt->execute();
        return $stmt->fetchColumn();

    }
    public function defaultPlannerRecipes($userId, $day): array{
        $results = [];
        $stmt = $this->database->connect()->prepare('
            SELECT * FROM recipes r JOIN planner p on r.id=p.recipe_id WHERE p.user_id = :userId AND p.day =:day
        ');
        $stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
        $stmt->bindParam(':day', $day, PDO::PARAM_STR);
        $stmt->execute();
        $recipes = $stmt->fetchAll(PDO::FETCH_ASSOC);
        foreach ($recipes as $recipe) {
            $result = new Recipe(
                $recipe['author_id'],
                $recipe['name'],
                $recipe['description'],
                $recipe['img_name'],
                $recipe['recipe_id']
            );
            $results[] = $result;
        }
        return $results;
    }
    public function getShoppingList($userId): array{
        $results = [];
        $stmt = $this->database->connect()->prepare('
            SELECT * FROM ingredients i JOIN planner p on i.recipe_id=p.recipe_id WHERE p.user_id = :userId
        ');
        $stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
        $stmt->execute();
        $recipes = $stmt->fetchAll(PDO::FETCH_ASSOC);
        foreach ($recipes as $recipe) {
            $result = new Ingredient(
                $recipe['recipe_id'],
                $recipe['amount'],
                $recipe['product']
            );
            $results[] = $result;
        }
        return $results;
    }
    public function addToPlanner($userId, $recipeId, $day){
        $stmt = $this->database->connect()->prepare('
            INSERT INTO planner (user_id, recipe_id, day)
            VALUES (?, ?, ?)
        ');
        $stmt->execute([
            $userId,
            $recipeId,
            $day
        ]);
    }
    public function rate($userId, $recipeId, $rating)
    {
        $stmt = $this->database->connect()->prepare('
            INSERT INTO ratings (recipe_id, user_id, rating)
            VALUES (?, ?, ?)
        ');
        $stmt->execute([
            $recipeId,
            $userId,
            $rating
        ]);
    }
    private function alreadyRated($recipeId, $userId): int{
        $stmt = $this->database->connect()->prepare('
            SELECT COUNT(*) FROM ratings WHERE recipe_id = :recipeId AND user_id = :userId
        ');
        $stmt->bindParam(':recipeId', $recipeId, PDO::PARAM_INT);
        $stmt->bindParam(':userId', $userId, PDO::PARAM_INT);
        $stmt->execute();
        return $stmt->fetchColumn();
    }
}