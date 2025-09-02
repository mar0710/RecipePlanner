package com.example.demo.controller;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/recipes")
public class CommentController {
    @Autowired
    CommentService commentService;


    @PostMapping("/{id}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long id, @RequestParam("content") String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        commentService.addComment(id, auth, content); // just save, ignore return
        return ResponseEntity.ok("Comment added successfully!");
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}/deletecomment/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId){
        commentService.deleteById(commentId);
    }

}