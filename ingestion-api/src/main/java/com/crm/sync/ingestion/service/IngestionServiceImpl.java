package com.crm.sync.ingestion.service;

import com.crm.sync.ingestion.dto.CampaignMessageDto;
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
@Slf4j
public class IngestionServiceImpl implements IngestionService {

    @Value("${crm.api.crm-campaigns-queue}")
    private String crmCampaignsQueue;

    private final RabbitTemplate rabbitTemplate;

    public IngestionServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
	 * Processes a list of CampaignRequest objects and enqueues them to RabbitMQ.
	 * For each request, a corresponding CampaignMessage is created and sent to the configured queue.
	 *
	 * @param requests the list of campaign requests to be processed and enqueued
	 */
    @Override
    public void processAndEnqueue(List<CampaignMessageDto> requests) {
        if (requests == null) {
        	log.warn("Received null campaign requests list, skipping processing");
        	return;
        }

        log.info("Processing {} campaign requests for enqueuing", requests.size());
        for (CampaignMessageDto message : requests) {

            log.debug("Enqueuing campaign message: {}", message);
            rabbitTemplate.convertAndSend(crmCampaignsQueue, message);
            log.info("Enqueued campaign message for campaignId: {}", message.getCampaignId());
        }
    }
}
