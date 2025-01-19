<?php

require 'Routing.php';

$path = trim($_SERVER['REQUEST_URI'], '/');
$path = parse_url( $path, PHP_URL_PATH);

Router::get('', 'DefaultController');
Router::post('login','SecurityController');
Router::get('main', 'DefaultController');
Router::post('signUp','SecurityController');
Router::get('myAccount','SecurityController');
Router::post('addRecipe','RecipeController');
Router::get('myAccountEdit','DefaultController');
Router::get('recipes','RecipeController');
Router::get('myRecipes','RecipeController');
Router::get('recipeDetails','RecipeController');
Router::post('addComment','RecipeController');
Router::post('logout','SecurityController');
Router::get('favorites','RecipeController');
Router::post('addToFavorites','RecipeController');
Router::get('planner','RecipeController');
Router::get('shoppingList','RecipeController');
Router::post('search','RecipeController');
Router::post('addToPlanner','RecipeController');
Router::post('rateRecipe','RecipeController');
Router::run($path);