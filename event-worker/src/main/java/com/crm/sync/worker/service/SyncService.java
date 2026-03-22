package com.crm.sync.worker.service;

import com.crm.sync.worker.dto.CampaignMessage;
import com.crm.sync.worker.entity.Campaign;

/** 
 * Service interface for synchronizing campaign data.
 **/
public interface SyncService {
    void synchronizeCampaign(CampaignMessage campaignMessage);
}
