package com.crm.sync.worker.listener;

import com.crm.sync.worker.dto.CampaignMessageDto;
import com.crm.sync.worker.service.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listener component for processing incoming campaign messages from RabbitMQ.
 */
@Component
@Slf4j
public class CrmCampaignListener {

    private SyncService syncService;

    public CrmCampaignListener(SyncService syncService){
        this.syncService = syncService;
    }

    /**
	 * Method to handle incoming campaign messages from RabbitMQ.
	 * This method is triggered whenever a new message is received on the configured queue
	 * and calls the synchronization service.
	 *
	 * @param campaignMessage the incoming campaign message to be processed
	 */
    @RabbitListener(queues = "${crm.api.crm-campaigns-queue}")
    public void onCampaignMessageReceived(CampaignMessageDto campaignMessage) {
        if (campaignMessage == null) {
        	log.warn("Received null campaign message, skipping processing");
        	return;
        }

        log.debug("Received campaign message for campaignId: {}", campaignMessage.getCampaignId());
        syncService.synchronizeCampaign(campaignMessage);
        log.info("Processed campaign message for campaignId: {}", campaignMessage.getCampaignId());
    }
}
