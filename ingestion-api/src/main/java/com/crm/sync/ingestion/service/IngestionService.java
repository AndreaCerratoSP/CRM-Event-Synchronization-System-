package com.crm.sync.ingestion.service;

import com.crm.sync.ingestion.dto.CampaignRequest;
import java.util.List;

/**
 * Service interface for processing and enqueuing campaign synchronization requests.
 **/
public interface IngestionService {
    void processAndEnqueue(List<CampaignRequest> requests);
}
