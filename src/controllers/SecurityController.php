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

        // Store the session in the database
        $stmt = $this->database->connect()->prepare('INSERT INTO sessions (session_id, user_id, last_activity) VALUES (:session_id, :user_id, :last_activity)');
        $stmt->execute([
            ':session_id' => $sessionId,
            ':user_id' => $user['id'],
            ':last_activity' => date('Y-m-d H:i:s')
        ]);

        // Set the session cookie
        setcookie('session_id', $sessionId, [
            'expires' => time() + 3600, // 1 hour
            'path' => '/',
            'secure' => true,
            'httponly' => true,
            'samesite' => 'Strict'
        ]);
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
        //TODO try to use better hash function
        $user = new User($email, $pass, $username);

        $this->userRepository->addUser($user);

        return $this->render('login', ['messages' => ['You\'ve been succesfully registrated!']]);
    }

}