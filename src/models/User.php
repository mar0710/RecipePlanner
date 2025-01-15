<?php

namespace models;

class User
{
    private int $id;
    private string $userName;
    private string $password;
    private string $email;

    public function __construct(int $id, $email, string $password, string $userName)
    {
        $this->id = $id;
        $this->userName = $userName;
        $this->password = $password;
        $this->email = $email;
    }

    public function getId(): int
    {
        return $this->id;
    }

    public function setId(int $id): void
    {
        $this->id = $id;
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