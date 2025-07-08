// package com.example.dataService.controller;

// import com.example.dataService.dto.WebScrapeRequest;
// import com.example.dataService.service.ScraperService;
// import com.example.dataService.service.SummarizationService;
// import com.example.dataService.Model.WebScrape;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api")
// public class ScraperController {

//     @Autowired
//     private ScraperService scraperService;

//     @Autowired
//     private SummarizationService summarizationService;

//     // Endpoint to request scraping and summarization
//     @PostMapping("/url")
//     public ResponseEntity<?> summarize(@RequestBody WebScrapeRequest request) {
//         try {
//             String url = request.getUrl(); // Extract URL from the request body
//             Long userId = request.getUserId();// ✅ Get userId
//             Boolean isImportantNotesNeeded = request.getIsImportantNotesNeeded(); // Extract isImportantNotesNeeded

//             // Step 1: Scrape data using ScraperService
//             WebScrape webScrape = scraperService.scrapeWebsite(url, userId);

//             if (webScrape == null) {
//                 return ResponseEntity.status(500).body("Failed to fetch sCcraped data.");
//             }

//             // Step 2: Call SummarizationService to summarize scraped data
//             WebScrape updatedWebScrape = summarizationService.summarizeScrapedData(webScrape.getId(), isImportantNotesNeeded);

//             return ResponseEntity.ok(updatedWebScrape);
//         } catch (Exception e) {
//             e.printStackTrace();
//             return ResponseEntity.status(500).body("Error occurred during the scraping or summarization process.");
//         }
//     }
// }
