<?php

use models\Comment;
use models\Ingredient;
use models\Recipe;

require_once 'AppController.php';
require_once __DIR__.'/../models/Recipe.php';
require_once __DIR__ . '/../models/Ingredient.php';
require_once __DIR__.'/../models/Comment.php';
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
        session_start();
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
        $fileName='default_photo.png';
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
        $recipe = new Recipe($_SESSION['user_id'], $_POST['recipeName'], $_POST['description'], $fileName, 0);
        $recipe_id=$this->recipeRepository->addRecipe($recipe);
        foreach ($amounts as $index => $amount) {
            $product = $products[$index];
            $amount = $amounts[$index];
            $ingredient = new Ingredient($recipe_id, $amount, $product);
            $this->recipeRepository->addIngredient($ingredient);
        }
        $this->myRecipes();
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
    public function myRecipes()
    {
        session_start();
        $myRecipes=$this->recipeRepository->myRecipes($_SESSION['user_id']);
        $this->render('myRecipes', ['myRecipes' => $myRecipes]);

    }
    public function recipeDetails()
    {
        session_start();
        $recipe=$this->recipeRepository->getRecipe($_GET['id']);
        $ingredients=$this->recipeRepository->getIngredients($_GET['id']);
        $comments=$this->recipeRepository->getComments($_GET['id']);
        $this->render('recipeDetails', ['recipe' => $recipe, 'ingredients'=> $ingredients, 'comments' => $comments]);
    }
    private function recipeDetailsWithId($recipeId)
    {
        $recipe = $this->recipeRepository->getRecipe($recipeId);
        $ingredients = $this->recipeRepository->getIngredients($recipeId);
        $comments = $this->recipeRepository->getComments($recipeId);

        $this->render('recipeDetails', [
            'recipe' => $recipe,
            'ingredients' => $ingredients,
            'comments' => $comments
        ]);
    }
    public function addComment()
    {
        session_start();
        if(!$this->isPost()){
            return $this->render('recipeDetails', ['message' => "Error:method not post"]);
        }
        $recipeId=$_POST['id'];
        $content=$_POST['comment'];
        $comment=new Comment($recipeId, $content, $_SESSION['user_id']);
        $this->recipeRepository->addComment($comment);
        return $this->recipeDetailsWithId($recipeId );
    }
    public function favorites()
    {
        session_start();
        $recipes=$this->recipeRepository->myFavorites($_SESSION['user_id']);
        $this->render('favorites', ['recipes' => $recipes]);
    }
    public function addToFavorites(){
        session_start();
        $recipeId=$_POST['recipeId'];
        echo "      user id: :".$_SESSION['user_id'];
        echo "      recipe id: :".$recipeId;
        $this->recipeRepository->addToFavorites($_SESSION['user_id'], $recipeId);
        $this->recipeDetailsWithId($recipeId );
    }
    public function planner()
    {
        session_start();
        $recipes1=$this->recipeRepository->defaultPlannerRecipes($_SESSION['user_id'],"Monday");
        $recipes2=$this->recipeRepository->defaultPlannerRecipes($_SESSION['user_id'],"Tuesday");
        $recipes3=$this->recipeRepository->defaultPlannerRecipes($_SESSION['user_id'],"Wednesday");
        $recipes4=$this->recipeRepository->defaultPlannerRecipes($_SESSION['user_id'],"Thursday");
        $recipes5=$this->recipeRepository->defaultPlannerRecipes($_SESSION['user_id'],"Friday");
        $recipes6=$this->recipeRepository->defaultPlannerRecipes($_SESSION['user_id'],"Saturday");
        $recipes7=$this->recipeRepository->defaultPlannerRecipes($_SESSION['user_id'],"Sunday");

        $this->render('planner', ['recipes1' => $recipes1, 'recipes2'=>$recipes2, 'recipes3'=>$recipes3, 'recipes4'=>$recipes4, 'recipes5'=>$recipes5, 'recipes6'=>$recipes6, 'recipes7'=>$recipes7]);

    }
    public function addToPlanner(){
        session_start();
        $this->recipeRepository->addToPlanner($_SESSION['user_id'],$_POST['recipeId'], $_POST['day']);
        $this->recipeDetailsWithId($_POST['recipeId'] );

    }
    public function shoppingList(){
        session_start();
        $products=$this->recipeRepository->getShoppingList($_SESSION['user_id']);
        $this->render('shoppingList', ['products' => $products]);
    }
    public function search()
    {
        $recipes=$this->recipeRepository->getRecipeByName($_POST['key']);
        $this->render('recipes', ['recipes' => $recipes]);
    }

    public function rateRecipe(){
        session_start();
        $rating=$_POST['rating'];
        $recipeId=$_POST['recipeId'];
        $this->recipeRepository->rate($_SESSION['user_id'], $_POST['recipeId'], $rating);
        $this->recipeDetailsWithId($recipeId);

    }
}
