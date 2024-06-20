package com.example.JWTImplemenation.Util;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public User getUserFromToken(String authToken) {
        // Extract username from token (assuming it's a JWT)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);
    }
}
