<?php

require_once 'AppController.php';

class DefaultController extends AppController {

    public function index()
    {
        $this->render('login');
    }

    public function main()
    {
        $this->render('main');
    }
    public function signUp()
    {
        $this->render('signUp');
    }
    public function myAccount()
    {
        $this->render('myAccount');
    }
    public function myAccountEdit()
    {
        $this->render('myAccountEdit');
    }
    public function recipes()
    {
        $this->render('recipes');
    }
    public function myRecipes()
    {
        $this->render('myRecipes');
    }
    public function addRecipe()
    {
        $this->render('addRecipe');
    }
}