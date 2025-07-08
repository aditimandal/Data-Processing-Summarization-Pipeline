package com.example.dataService.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WebScrape> webScrapes = new ArrayList<>();


    // Default Constructor
    public User() {
    }

    // Parameterized Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;  // This should be the hashed password
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;  // This should be the hashed password
    }

    public List<WebScrape> getWebScrapes() {
        return webScrapes;
    }

    public void setWebScrapes(List<WebScrape> webScrapes) {
        this.webScrapes = webScrapes;
    }

}
