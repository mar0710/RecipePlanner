package com.example.demo;

public class PlannerNotFoundException extends RuntimeException{
    public PlannerNotFoundException(Long id) {
        super("Planner not found for: " + id);
    }
}
