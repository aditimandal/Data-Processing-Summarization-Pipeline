package com.example.dataService.repository;

import com.example.dataService.Model.WebScrape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface WebScrapeRepository extends JpaRepository<WebScrape, Long> {
    Optional<WebScrape> findByUrlAndUserId(String url, Long userId);
    Optional<WebScrape> findByUrl(String url);

}

