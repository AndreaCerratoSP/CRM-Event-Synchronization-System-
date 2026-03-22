package com.crm.sync.worker.listener;

import com.crm.sync.worker.dto.CampaignMessage;
import com.crm.sync.worker.entity.Attendee;
import com.crm.sync.worker.entity.Campaign;
import com.crm.sync.worker.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Listener component for processing incoming campaign messages from RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CrmCampaignListener {

    @Autowired
    private SyncService syncService;

    /**
	 * Method to handle incoming campaign messages from RabbitMQ.
	 * This method is triggered whenever a new message is received on the configured queue
	 * and calls the synchronization service.
	 *
	 * @param campaignMessage the incoming campaign message to be processed
	 */
    @RabbitListener(queues = "crm.campaigns.queue")
    public void onCampaignMessageReceived(CampaignMessage campaignMessage) {
        if (campaignMessage == null) {
        	log.warn("Received null campaign message, skipping processing");
        	return;
        }

        log.debug("Received campaign message for campaignId: {}", campaignMessage.getCampaignId());
        syncService.synchronizeCampaign(campaignMessage);
        log.info("Processed campaign message for campaignId: {}", campaignMessage.getCampaignId());
    }
}
