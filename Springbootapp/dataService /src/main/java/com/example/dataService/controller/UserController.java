package com.example.dataService.controller;

import com.example.dataService.Model.User;
import com.example.dataService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dataService.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
// Allow frontend access
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            return ResponseEntity.ok(Map.of("exists", user.isPresent()));
        } catch (Exception e) {
            logger.error("Error checking email existence: ", e);
            return ResponseEntity.status(500).body("Error checking email: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        try {
            logger.info("=== Login Request Debug ===");
            logger.info("Received login request for email: {}", request.get("email"));
            
            // Validate request
            if (request == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Request body is required"
                ));
            }

            // Check for required fields
            if (!request.containsKey("email") || !request.containsKey("password")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Email and password are required"
                ));
            }

            String email = request.get("email");
            String password = request.get("password");

            // Validate email and password are not empty
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Email cannot be empty"
                ));
            }

            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Password cannot be empty"
                ));
            }

            // Attempt login
            Optional<User> userOpt = userService.loginUser(email, password);
            
            if (userOpt.isPresent()) {
                User loggedInUser = userOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Login successful");
                response.put("user", Map.of(
                    "id", loggedInUser.getId(),
                    "username", loggedInUser.getUsername(),
                    "email", loggedInUser.getEmail()
                ));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Invalid email or password"
                ));
            }
        } catch (Exception e) {
            logger.error("Unexpected error during login process: ", e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "message", "Internal server error: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        try {
            logger.info("Received signup request for user: {}", user.getEmail());
            
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                logger.error("Email is null or empty in signup request");
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Email is required"
                ));
            }

            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                logger.error("Username is null or empty in signup request");
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Username is required"
                ));
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                logger.error("Password is null or empty in signup request");
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Password is required"
                ));
            }

            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                logger.warn("Signup attempt with existing email: {}", user.getEmail());
                return ResponseEntity.status(400).body(Map.of(
                    "status", "error",
                    "message", "User already exists!"
                ));
            }

            User newUser = userService.registerUser(
                user.getUsername().trim(), 
                user.getEmail().trim(),
                user.getPassword().trim()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "User registered successfully");
            response.put("user", Map.of(
                "id", newUser.getId(),
                "username", newUser.getUsername(),
                "email", newUser.getEmail()
            ));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during signup: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Unexpected error during signup process: ", e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "message", "Internal server error during signup"
            ));
        }
    }
}
