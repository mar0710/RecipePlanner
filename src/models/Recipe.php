<?php

namespace models;

class Recipe
{
    private int $user_id;

    private string $name;
    private string $description;
    private string $image;

    public function __construct(int $user_id, string $name, string $description, string $image){
        $this->user_id = $user_id;
        $this->name = $name;
        $this->description = $description;
        $this->image = $image;
    }

    public function getImage():string
    {
        return $this->image;
    }

    public function setImage(string $image): void
    {
        $this->image = $image;
    }
    public function getName(): string
    {
        return $this->name;
    }

    public function getDescription(): string
    {
        return $this->description;
    }

    public function setName(string $name): void
    {
        $this->name = $name;
    }


    public function setDescription(string $description): void
    {
        $this->description = $description;
    }
}