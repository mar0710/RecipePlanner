<?php

namespace models;

class Recipe
{
    private int $id;
    private int $user_id;
    private string $name;
    private string $description;
    private string $image;
    private float $rating;

    public function getId(): int
    {
        return $this->id;
    }
    public function setId(int $id): void
    {
        $this->id = $id;
    }

    public function getUserId(): int
    {
        return $this->user_id;
    }

    public function setUserId(int $user_id): void
    {
        $this->user_id = $user_id;
    }

    public function getRating(): float
    {
        return $this->rating;
    }

    public function setRating(float $rating): void
    {
        $this->rating = $rating;
    }



    public function __construct(int $user_id, string $name, string $description, string $image, int $id){
        $this->user_id = $user_id;
        $this->name = $name;
        $this->description = $description;
        $this->image = $image;
        $this->id = $id;
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