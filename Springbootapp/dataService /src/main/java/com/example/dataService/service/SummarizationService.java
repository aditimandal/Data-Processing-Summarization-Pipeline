
package com.example.dataService.service;

import com.example.dataService.Model.WebScrape;
import com.example.dataService.exceptions.SummarizationException;
import com.example.dataService.repository.WebScrapeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SummarizationService {
    private static final Logger log = LoggerFactory.getLogger(SummarizationService.class);

    @Value("${flask.summarizer.url}")
    private String flaskSummarizerUrl;

    private final WebScrapeRepository webScrapeRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public SummarizationService(WebScrapeRepository webScrapeRepository, RestTemplate restTemplate) {
        this.webScrapeRepository = webScrapeRepository;
        this.restTemplate = restTemplate;
    }

    public WebScrape summarizeScrapedData(Long id, boolean isImportantNotesNeeded) {
        try {
            WebScrape webScrape = webScrapeRepository.findById(id)
                    .orElseThrow(() -> new SummarizationException("Scraped data not found for ID: " + id));

            validateScrapedData(webScrape);
.
            JsonNode response = callFlaskSummarizer(webScrape.getScrapedData(), isImportantNotesNeeded);
            updateWebScrape(webScrape, response, isImportantNotesNeeded);

            return webScrapeRepository.save(webScrape);

        } catch (HttpClientErrorException e) {
            log.error("HTTP Error from Flask API: {}", e.getResponseBodyAsString());
            throw new SummarizationException("Summarization API error", e);
        } catch (Exception e) {
            log.error("Summarization failed for ID: {}", id, e);
            throw new SummarizationException("Summarization process failed", e);
        }
    }

    private void validateScrapedData(WebScrape webScrape) {
        if (webScrape.getScrapedData() == null || webScrape.getScrapedData().isBlank()) {
            throw new SummarizationException("Empty scraped data for ID: " + webScrape.getId());
        }
    }

    private JsonNode callFlaskSummarizer(String scrapedData, boolean notesNeeded) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("text", scrapedData);
        requestBody.put("isImportantNotesNeeded", notesNeeded);

        log.debug("Sending summarization request for {} chars", scrapedData.length());
        ResponseEntity<String> response = restTemplate.postForEntity(
                flaskSummarizerUrl,
                new HttpEntity<>(requestBody, headers),
                String.class
        );

        return new ObjectMapper().readTree(response.getBody());
    }

    private void updateWebScrape(WebScrape webScrape, JsonNode response, boolean notesNeeded) {
        log.info("Updating WebScrape with ID: {} - Notes needed: {}", webScrape.getId(), notesNeeded);
        
        webScrape.setSummary(response.path("summary").asText());
        webScrape.setUpdatedAt(LocalDateTime.now());
        webScrape.setStatus(WebScrape.Status.SUMMARIZED);

        if (notesNeeded) {
            if (response.has("important_points") && !response.get("important_points").isNull()) {
                String importantPoints = response.get("important_points").asText();
                log.info("Setting important notes for WebScrape ID: {}", webScrape.getId());
                webScrape.setNotes(importantPoints);
            } else {
                log.warn("Important points requested but not found in response for WebScrape ID: {}", webScrape.getId());
                webScrape.setNotes("No important points available");
            }
        } else {
            log.info("Important notes not requested for WebScrape ID: {}", webScrape.getId());
            webScrape.setNotes(null);
        }
    }
}