package com.crm.sync.worker.service;

import com.crm.sync.worker.dto.CampaignMessageDto;

/** 
 * Service interface for synchronizing campaign data.
 **/
public interface SyncService {
    void synchronizeCampaign(CampaignMessageDto campaignMessage);
}
