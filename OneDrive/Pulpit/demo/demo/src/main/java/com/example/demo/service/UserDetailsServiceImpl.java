package com.example.demo.service;
import com.example.demo.UserNotFoundException;
import com.example.demo.dtos.Profile;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return CustomUserDetails.fromUser(user);
    }


    public void deleteUser(Authentication auth){
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        userRepository.deleteById(userId);
    }
    public Profile getProfile(Authentication auth){
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setUsername(user.getUsername());
        return profile;
    }
}