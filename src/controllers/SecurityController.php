<?php

use models\User;

require_once 'AppController.php';
require_once __DIR__.'/../models/User.php';
require_once __DIR__.'/../repository/UserRepository.php';
class SecurityController extends AppController
{
    private $userRepository;

    public function __construct()
    {
        parent::__construct();
        $this->userRepository = new UserRepository();
    }
    public function login(){
        session_start();
        if(!$this->isPost()){
            return $this->render('login');
        }

        $email = $_POST["email"];
        $password = $_POST["password"];
        $user = $this->userRepository->getUser($email);
        if (!$user) {
            return $this->render('login', ['messages' => ['User not found!']]);
        }
        if($user->getEmail() !== $email){
            return $this->render('login',['messages' => ['user with this email does not exist']]);
        }
        if(!password_verify($password, $user->getPassword())) {
            return $this->render('login',['messages' => ['wrong password or email']]);
        }
        $sessionId = bin2hex(random_bytes(32));
        $_SESSION['user_id'] = $user->getId();
        $this->userRepository->activateSession($sessionId, $user->getId());
        // Set the session cookie
        setcookie('session_id', $sessionId, [
            'expires' => time() + 3600, // 1 hour
            'path' => '/',
            'secure' => true,
            'httponly' => true,
            'samesite' => 'Strict'
        ]);
        if (!isset($_SESSION['user_id'])) {
            die("Error: User is not logged in. Please log in first.");
        }
        $url="http://$_SERVER[HTTP_HOST]";
        header("Location: $url/main");
    }
    public function signUp()
    {
        if (!$this->isPost()) {
            return $this->render('signUp');
        }

        $email = $_POST['email'];
        $password = $_POST['password'];
        $confirmedPassword = $_POST['confirmedPassword'];
        $username = $_POST['username'];

        if ($password !== $confirmedPassword) {
            return $this->render('signUp', ['messages' => ['Please provide proper password']]);
        }

        if($this->userRepository->countUsers($email, 'email') > 0){
            return $this->render('signUp', ['messages' => ['This email address is alreday in use']]);
        }
        if($this->userRepository->countUsers($username, 'username') > 0){
            return $this->render('signUp', ['messages' => ['This usernmae is alreday in use']]);
        }

        $pass = password_hash($password, PASSWORD_DEFAULT);

        $user = new User(0, $email, $pass, $username);

        $this->userRepository->addUser($user);

        return $this->render('login', ['messages' => ['You\'ve been succesfully registrated!']]);
    }
    public function myAccount(){
        session_start();
        $user = $this->userRepository->getUserById($_SESSION['user_id']);
        return $this->render('myAccount', ['user' => $user]);
    }

    public function logout(){
        session_start();
        session_destroy();
        $this->render('login');
        exit();
    }

}