package com.crm.sync.ingestion.controller;

import com.crm.sync.ingestion.dto.CampaignRequest;
import com.crm.sync.ingestion.service.IngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling CRM campaign synchronization requests.
 * This controller exposes an endpoint to receive campaign data, validate it, and enqueue it for processing.
 **/
@RestController
@RequestMapping("/api/v1/crm")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CrmController {

    @Autowired
    private final IngestionService ingestionService;

    /**
	 * Endpoint to receive a list of campaign requests, validate them, and enqueue them for processing.
	 * The endpoint accepts POST requests at /api/v1/crm/sync with a JSON body containing a list of CampaignRequest objects.
	 *
	 * @param requests the list of campaign requests to be processed
	 * @return a ResponseEntity with HTTP status 202 Accepted if the requests are successfully enqueued
	 */
    @PostMapping("/sync")
    public ResponseEntity<Void> sync(@RequestBody @Valid List<CampaignRequest> requests) {
    	log.info("Received {} campaign sync requests", requests.size());    	
        ingestionService.processAndEnqueue(requests);
        log.info("Successfully enqueued {} campaign sync requests", requests.size());
        
        return ResponseEntity.accepted().build();
    }
}
