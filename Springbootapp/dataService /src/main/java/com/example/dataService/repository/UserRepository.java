package com.example.dataService.repository;

import com.example.dataService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Additional custom query methods (if needed) can be defined here.
    Optional<User> findByEmail(String email);
}
