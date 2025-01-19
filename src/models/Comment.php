<?php

namespace models;

class Comment
{
    private int $recipe_id;
    private string $comment;
    private string $userName;
    private int $author_id;
    private string $date;
    public function __construct(int $recipe_id, string $comment, int $author_id){
        $this->recipe_id = $recipe_id;
        $this->comment = $comment;
        $this->author_id = $author_id;
    }

    public function getDate(): string
    {
        return $this->date;
    }

    public function setDate(string $date): void
    {
        $this->date = $date;
    }

    public function getUserName(): string
    {
        return $this->userName;
    }

    public function setUserName(string $userName): void
    {
        $this->userName = $userName;
    }

    public function getRecipeId(): int
    {
        return $this->recipe_id;
    }

    public function getComment(): string
    {
        return $this->comment;
    }

    public function setComment(string $comment): void
    {
        $this->comment = $comment;
    }

    public function getAuthorId(): int
    {
        return $this->author_id;
    }

    public function setAuthorId(int $author_id): void
    {
        $this->author_id = $author_id;
    }

}