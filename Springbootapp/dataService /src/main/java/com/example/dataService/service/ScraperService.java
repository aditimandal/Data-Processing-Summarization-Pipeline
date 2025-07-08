package com.example.dataService.service;

import com.example.dataService.Model.User;
import com.example.dataService.Model.WebScrape;
import com.example.dataService.exceptions.ScraperServiceException;
import com.example.dataService.exceptions.UserNotFoundException;
import com.example.dataService.repository.UserRepository;
import com.example.dataService.repository.WebScrapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ScraperService {

    @Value("${scraper.service.url}")
    private String scraperServiceUrl;

    @Autowired
    private WebScrapeRepository webScrapeRepository;

    @Autowired
    private UserRepository userRepository;

    public WebScrape scrapeWebsite(String url, Long userId) {
        validateUrl(url); // Validate URL format

        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = scraperServiceUrl + "?url=" + url;
            String scrapedData = restTemplate.getForObject(apiUrl, String.class);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User ID " + userId + " not found"));

            Optional<WebScrape> existingEntry = webScrapeRepository.findByUrlAndUserId(url, userId);

            if (existingEntry.isPresent()) {
                WebScrape scrape = existingEntry.get();
                scrape.setScrapedData(scrapedData);
                scrape.setStatus(WebScrape.Status.UPDATED);// Using enum
                scrape.setUpdatedAt(LocalDateTime.now());
                return webScrapeRepository.save(scrape);
            }

            WebScrape newScrape = new WebScrape();
            newScrape.setUrl(url);
            newScrape.setScrapedData(scrapedData);
            newScrape.setStatus(WebScrape.Status.NEW);
            newScrape.setCreatedAt(LocalDateTime.now());
            newScrape.setUser(user);
            return webScrapeRepository.save(newScrape);

        } catch (RestClientException e) {
            throw new ScraperServiceException("Failed to call scraper API: " + e.getMessage(), e);
        } catch (UserNotFoundException e) {
            throw e; // Re-throw specific exception
        }
    }

    private void validateUrl(String url) {
        // Regex for HTTP/HTTPS URLs with domains, paths, and query params
        String regex = "^(https?://)(www\\.)?[a-zA-Z0-9@:%._+~#?&/=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._+~#?&/=]*)$";
        if (!url.matches(regex)) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
    }
}