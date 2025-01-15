<?php

require 'Routing.php';

$path = trim($_SERVER['REQUEST_URI'], '/');
$path = parse_url( $path, PHP_URL_PATH);

Router::get('', 'DefaultController');
Router::post('login','SecurityController');
Router::get('main', 'DefaultController');
Router::post('signUp','SecurityController');
Router::get('myAccount','DefaultController');
Router::post('addRecipe','RecipeController');
Router::get('myAccountEdit','DefaultController');
Router::get('recipes','DefaultController');
Router::get('myRecipes','DefaultController');

Router::run($path);