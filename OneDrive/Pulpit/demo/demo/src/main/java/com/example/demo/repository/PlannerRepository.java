package com.example.demo.repository;


import com.example.demo.model.Planner;
import com.example.demo.model.Recipe;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    List<Planner> findByUserAndDay(User user, String day);

    List<Planner> findByUserId(Long userId);
}