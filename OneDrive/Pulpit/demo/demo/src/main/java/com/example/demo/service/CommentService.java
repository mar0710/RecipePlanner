package com.example.demo.service;
import com.example.demo.RecipeNotFoundException;
import com.example.demo.UserNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;;import java.time.LocalDate;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<String> addComment(Long recipeId, Authentication auth, String content) {
        Long user_id = ((CustomUserDetails) auth.getPrincipal()).getId();
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException(user_id));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setRecipe(recipe);
        comment.setCreated_at(LocalDate.now());


        commentRepository.save(comment);
        return ResponseEntity.ok("Comment added successfully!");
    }
    @Transactional
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
