<?php

namespace models;

class User
{
    private string $userName;
    private string $password;
    private string $email;

    public function __construct(string $email, string $password, string $userName)
    {
        $this->userName = $userName;
        $this->password = $password;
        $this->email = $email;
    }

    public function getUserName(): string
    {
        return $this->userName;
    }

    public function setUserName(string $userName): void
    {
        $this->userName = $userName;
    }

    public function getPassword(): string
    {
        return $this->password;
    }

    public function setPassword(string $password)
    {
        $this->password = $password;
    }

    public function getEmail(): string
    {
        return $this->email;
    }

    public function setEmail(string $email)
    {
        $this->email = $email;
    }

}