package com.example.demo.repository;

import com.example.demo.model.Recipe;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByIsApprovedTrue();
    List<Recipe> findByIsApprovedFalse();

    @Query("SELECT DISTINCT r FROM Recipe r LEFT JOIN r.ingredients i " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.product) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Recipe> searchByKeyword(@Param("keyword") String keyword);

    //@Query("SELECT f.recipe FROM Favorite f WHERE f.user.id = :userId")
   // List<Recipe> getFavoriteByUser(User user);

    List<Recipe> findByAuthorId(Long userId);
}