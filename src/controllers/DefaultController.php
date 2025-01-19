<?php

require_once 'AppController.php';

class DefaultController extends AppController {

    public function index()
    {
        $this->render('login');
    }
    public function main(){
        return $this->render('main');
    }

    public function myAccountEdit()
    {
        $this->render('myAccountEdit');
    }

}