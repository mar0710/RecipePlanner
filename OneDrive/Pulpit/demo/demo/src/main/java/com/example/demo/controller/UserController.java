package com.example.demo.controller;
import com.example.demo.dtos.Profile;
import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserDetailsServiceImpl userService;

    @DeleteMapping
    void deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUser(auth);
    }

    @GetMapping
    public ResponseEntity<Profile> getProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userService.getProfile(auth));
    }
}
