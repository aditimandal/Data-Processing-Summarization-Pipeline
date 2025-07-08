package com.example.dataService.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "web_scrape")
public class WebScrape {

    public enum Status {
        NEW,          // Initial scrape
        UPDATED,      // Existing scrape updated
        SUMMARIZED,
        FAILED        // Scrape failed
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "scraped_data", columnDefinition = "TEXT")
    private String scrapedData;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "Imp_Notes", columnDefinition = "TEXT", nullable = true)
    private String notes;

    @Enumerated(EnumType.STRING) // Store enum as a string in the database
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //This setup creates a foreign key (user_id) in the web_scrape table and tells JPA how WebScrape belongs to a User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;


    // Default constructor
    public WebScrape() {
        this.createdAt = LocalDateTime.now();
    }


    // Parameterized Constructor
    public WebScrape(String url, String scrapedData, String summary, String notes, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.url = url;
        this.scrapedData = scrapedData;
        this.summary = summary;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScrapedData() {
        return scrapedData;
    }

    public void setScrapedData(String scrapedData) {
        this.scrapedData = scrapedData;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }



    // After (enum type):
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
