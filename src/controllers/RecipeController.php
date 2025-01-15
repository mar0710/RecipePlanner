<?php

use models\Ingredient;
use models\Recipe;

require_once 'AppController.php';
require_once __DIR__.'/../models/Recipe.php';
require_once __DIR__ . '/../models/Ingredient.php';
require_once __DIR__.'/../repository/RecipeRepository.php';
class RecipeController extends AppController
{
    const MAX_FILE_SIZE = 1024 * 1024;
    const SUPPORTED_TYPES = ['image/png', 'image/jpeg'];
    const UPLOAD_DIRECTORY = '/../Public/uploads/';

    private $message = [];
    private $recipeRepository;
    public function __construct()
    {
        parent::__construct();
        $this->recipeRepository = new RecipeRepository();
    }
    public function recipes(){
        $recipes = $this->recipeRepository->getRecipes();
        $this->render('recipes', ['recipes' => $recipes]);
    }
    public function addRecipe()
    {
        session_start();
        if(!$this->isPost()){

            return $this->render('addRecipe');
        }
        $amounts = $_POST['amount'] ?? [];
        $products = $_POST['product'] ?? [];

        if (count($amounts) !== count($products) && count($amounts) > 0) {
            return $this->render('addRecipe', ['messages' => ['Error in ingredients']]);

        }
        if (isset($_FILES['file']) && $_FILES['file']['error'] === UPLOAD_ERR_OK){
            if(is_uploaded_file($_FILES['file']['tmp_name']) && $this->validate($_FILES['file'])) {
                move_uploaded_file(
                    $_FILES['file']['tmp_name'],
                    dirname(__DIR__) . self::UPLOAD_DIRECTORY . $_FILES['file']['name']
                );
                $fileName = $_FILES['file']['name'];
            }
            else {
                return $this->render('addRecipe', ['messages' => $this->message]);
            }
        }
        $recipe = new Recipe($_SESSION['user_id'], $_POST['recipeName'], $_POST['description'], $fileName);
        $recipe_id=$this->recipeRepository->addRecipe($recipe);
        foreach ($amounts as $index => $amount) {
            $product = $products[$index];
            $amount = $amounts[$index];
            $ingredient = new Ingredient($recipe_id, $amount, $product);
            $this->recipeRepository->addIngredient($ingredient);
        }
        return $this->render('myRecipes');
    }

    private function validate(array $file): bool
    {
        if ($file['size'] > self::MAX_FILE_SIZE) {
            $this->message[] = 'File is too large for destination file system.';
            return false;
        }

        if (!isset($file['type']) || !in_array($file['type'], self::SUPPORTED_TYPES)) {
            $this->message[] = 'File type is not supported.';
            return false;
        }
        return true;
    }
}
