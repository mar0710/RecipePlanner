<?php

use models\User;

require_once 'Repository.php';
require_once __DIR__.'/../models/User.php';
class UserRepository extends Repository
{
    public function getUser(string $email): ?User
    {
        $stmt = $this->database->connect()->prepare('SELECT * FROM public.users WHERE email = :email');
        $stmt->bindParam(':email', $email,PDO::PARAM_STR);
        $stmt->execute();

        $user = $stmt->fetch(PDO::FETCH_ASSOC);
        if(!$user){
            return null;
        }
        return new User(
            $user['email'],
            $user['password'],
            $user['username']
        );
    }
    public function addUser(User $user): void
    {
        $stmt = $this->database->connect()->prepare('
            INSERT INTO public.users (username, email, password)
            VALUES (?, ?, ?)
        ');

        $stmt->execute([
            $user->getUsername(),
            $user->getEmail(),
            $user->getPassword()
        ]);
    }
    public function countUsers(string $data, string $columnName):int {
        $stmt = $this->database->connect()->prepare('SELECT COUNT(*) FROM public.users WHERE '.$columnName.' = :data');
        $stmt->bindParam(':data', $data,PDO::PARAM_STR);
        $stmt->execute();
        return $stmt->fetchColumn();
    }

}