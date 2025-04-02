package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="recipes")
public class Recipe {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "imgname", nullable = false)
    private String imgName;
    @Column(name = "rating", nullable = false)
    private double rating;
    @Column(name = "posttime", nullable = false)
    private String postTime;
    @Column(name = "authorid", nullable = false)
    private Long authorId;


    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }



//    @OneToMany(mappedBy = "recipeId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Ingredient> ingredients;

    public Recipe() {}

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Recipe(String name, String description/*, List<Ingredient> ingredients*/) {
        this.name = name;
        this.description = description;
//        this.ingredients = ingredients;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
//    public List<Ingredient> getIngredients() { return ingredients; }
//    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
}
