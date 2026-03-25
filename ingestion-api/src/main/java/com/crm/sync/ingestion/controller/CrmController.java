package com.crm.sync.ingestion.controller;

import com.crm.sync.ingestion.dto.CampaignMessageDto;
import com.crm.sync.ingestion.service.IngestionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@Validated
@Slf4j
public class CrmController {

    private final IngestionService ingestionService;

    public CrmController(IngestionService ingestionService){
        this.ingestionService = ingestionService;
    }

    /**
	 * Endpoint to receive a list of campaign requests, validate them, and enqueue them for processing.
	 * The endpoint accepts POST requests at /api/v1/crm/sync with a JSON body containing a list of CampaignRequest objects.
	 *
	 * @param request the list of campaign requests to be processed
	 * @return a ResponseEntity with HTTP status 202 Accepted if the requests are successfully enqueued
	 */
    @PostMapping("/sync")
    public ResponseEntity<Void> sync(@RequestBody @Valid List<CampaignMessageDto> request) {
    	log.info("Received {} campaign sync requests", request.size());
        ingestionService.processAndEnqueue(request);
        log.info("Successfully enqueued {} campaign sync requests", request.size());
        
        return ResponseEntity.accepted().build();
    }
}
