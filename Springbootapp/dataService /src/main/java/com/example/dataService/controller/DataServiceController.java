package com.example.dataService.controller;


import com.example.dataService.Model.User;
import com.example.dataService.Model.WebScrape;
import com.example.dataService.repository.UserRepository;
import com.example.dataService.repository.WebScrapeRepository;
import com.example.dataService.service.ScraperService;
import com.example.dataService.service.SummarizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Allow React to call backend
public class DataServiceController {

    private static final Logger log = LoggerFactory.getLogger(DataServiceController.class);

    @Autowired
    private WebScrapeRepository webScrapeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScraperService scraperService;

    @Autowired
    private SummarizationService summarizationService;

    // ✅ Existing API to fetch summary and notes by URL
    @GetMapping("/summary")
    public WebScrape getSummaryByUrl(@RequestParam String url) {
        return webScrapeRepository.findByUrl(url)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"));
    }

    // ✅ New API to scrape and summarize data for a specific user
    @PostMapping("/scrape")
    public WebScrape scrapeAndSummarize(@RequestParam Long userId,
                                        @RequestParam String url,
                                        @RequestParam(defaultValue = "false") Boolean isImportantNotesNeeded) {
        log.info("Received scrape request - userId: {}, url: {}, isImportantNotesNeeded: {}", 
                userId, url, isImportantNotesNeeded);

        // 1. Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2. Scrape website data
        WebScrape webScrape = scraperService.scrapeWebsite(url, userId);

        // 3. Associate user to webScrape
        webScrape.setUser(user);
        webScrapeRepository.save(webScrape);

        // 4. Summarize the data
        log.info("Starting summarization for WebScrape ID: {} with important notes: {}", 
                webScrape.getId(), isImportantNotesNeeded);
        WebScrape summarized = summarizationService.summarizeScrapedData(webScrape.getId(), isImportantNotesNeeded);

        log.info("Summarization completed for WebScrape ID: {}", summarized.getId());
        return summarized;
    }

    // ✅ Optional: Fetch all scrapes done by a user
    @GetMapping("/user/{userId}/scrapes")
    public List<WebScrape> getAllScrapesForUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getWebScrapes();
    }

}

