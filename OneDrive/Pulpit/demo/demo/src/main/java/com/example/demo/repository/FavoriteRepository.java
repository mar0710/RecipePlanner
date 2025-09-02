package com.example.demo.repository;

import com.example.demo.model.Favorite;
import com.example.demo.model.User;
import com.example.demo.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);
    boolean existsByUserAndRecipe(User user, Recipe recipe);
    void deleteByUserAndRecipe(User user, Recipe recipe);

    boolean existsByUserIdAndRecipeId(Long id, Long userId);


    Optional<List<Favorite>> findByUserId(Long userId);
}
