package com.crm.sync.ingestion.service;

import com.crm.sync.ingestion.config.RabbitMQConfig;
import com.crm.sync.ingestion.dto.CampaignMessage;
import com.crm.sync.ingestion.dto.CampaignRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for processing campaign requests and enqueuing them to RabbitMQ.
 * This service takes a list of CampaignRequest objects and sends them to the configured RabbitMQ queue
 * for further processing by downstream services.
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionServiceImpl implements IngestionService {

    @Value("${crm.api.crm-campaigns-queue}")
    public String crmCampaignsQueue;

    private final RabbitTemplate rabbitTemplate;

    /**
	 * Processes a list of CampaignRequest objects and enqueues them to RabbitMQ.
	 * For each request, a corresponding CampaignMessage is created and sent to the configured queue.
	 *
	 * @param requests the list of campaign requests to be processed and enqueued
	 */
    @Override
    public void processAndEnqueue(List<CampaignRequest> requests) {
        if (requests == null) {
        	log.warn("Received null campaign requests list, skipping processing");
        	return;
        }

        log.info("Processing {} campaign requests for enqueuing", requests.size());
        for (CampaignRequest request : requests) {
            CampaignMessage message = CampaignMessage.builder()
                    .campaignId(request.getCampaignId())
                    .subCampaignId(request.getSubCampaignId())
                    .attendees(request.getAttendees())
                    .build();

            log.debug("Enqueuing campaign message: {}", message);
            rabbitTemplate.convertAndSend(crmCampaignsQueue, message);
            log.info("Enqueued campaign message for campaignId: {}", message.getCampaignId());
        }
    }
}
