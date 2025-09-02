package com.example.demo.repository;


import com.example.demo.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findByUserIdAndRecipeId(Long id, Long userId);

    boolean existsByUserIdAndRecipeId(Long id, Long userId);
}
