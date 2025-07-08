package com.example.dataService.service;

import com.example.dataService.Model.User;
import com.example.dataService.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> loginUser(String email, String password) {
        try {
            logger.info("=== Login Attempt Debug ===");
            logger.info("Attempting to find user with email: {}", email);
            
            // Validate input
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                logger.error("Email or password is empty");
                    return Optional.empty();
                }

            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(email.trim());
            
            if (!userOpt.isPresent()) {
                logger.warn("No user found with email: {}", email);
                return Optional.empty();
            }

            User user = userOpt.get();
            logger.info("Found user: {}", user.getEmail());

            // Compare passwords directly
            if (!password.trim().equals(user.getPassword())) {
                logger.warn("Password mismatch for user: {}", email);
                return Optional.empty();
            }

            logger.info("Login successful for user: {}", email);
            return Optional.of(user);

        } catch (Exception e) {
            logger.error("Error during login process: ", e);
            throw new RuntimeException("Error during login process: " + e.getMessage());
        }
    }

    public User registerUser(String username, String email, String password) {
        logger.info("=== Registration Debug ===");
        
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Validate email format
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Check for existing email
        if (userRepository.findByEmail(email.trim()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create and save user with plain text password
        User newUser = new User(username.trim(), email.trim(), password.trim());
        return userRepository.save(newUser);
    }
}
