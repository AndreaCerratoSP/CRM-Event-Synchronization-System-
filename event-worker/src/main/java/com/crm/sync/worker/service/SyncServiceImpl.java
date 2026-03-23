package com.crm.sync.worker.service;

import com.crm.sync.worker.dto.CampaignMessage;
import com.crm.sync.worker.entity.Attendee;
import com.crm.sync.worker.entity.Campaign;
import com.crm.sync.worker.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Implementation of the SyncService interface for synchronizing campaign data.
 * This service handles the logic for saving or updating campaign information in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SyncServiceImpl implements SyncService {

    @Autowired
    private CampaignRepository campaignRepository;

    /**
	 * Synchronizes the given campaign data with the database.
	 * If the campaign already exists, it will be updated; otherwise, a new record will be created.
	 *
	 * @param campaignMessage The campaign to be synchronized.
	 */
    @Override
    @Transactional
    public void synchronizeCampaign(CampaignMessage campaignMessage) {
        if (campaignMessage == null || campaignMessage.getCampaignId() == null) {
            log.warn("Received null campaign or campaign with null ID, skipping synchronization");
            return;
        }

        // Cerchiamo se la campagna esiste già
        Campaign campaign = campaignRepository.findById(campaignMessage.getCampaignId())
                .orElse(new Campaign());
        
        if (campaign.getId() == null) {
            campaign.setId(campaignMessage.getCampaignId());
            log.debug("Creating new campaign with ID: {}", campaign.getId());
        } else {
            log.debug("Updating existing campaign with ID: {}", campaign.getId());
        }
        
        campaign.setSubCampaignId(campaignMessage.getSubCampaignId());

        if (campaignMessage.getAttendees() != null) {
            java.util.List<Attendee> newAttendees = campaignMessage.getAttendees().stream().map(dto -> {
                Attendee attendee = new Attendee();
                attendee.setCn(dto.getCn());
                attendee.setFirstName(dto.getFirstName());
                attendee.setLastName(dto.getLastName());
                attendee.setBirthDate(dto.getBirthDate());
                attendee.setPartnerId(dto.getPartnerId());
                attendee.setIsCompanion(dto.getIsCompanion());
                attendee.setQrCode(dto.getQrCode());
                attendee.setCampaign(campaign);
                return attendee;
            }).collect(Collectors.toList());

            if (campaign.getAttendees() == null) {
                campaign.setAttendees(new java.util.ArrayList<>());
            }

            campaign.getAttendees().clear();
            campaign.getAttendees().addAll(newAttendees);
            
            log.debug("Mapped and updated {} attendees for campaign ID: {}", campaign.getAttendees().size(), campaign.getId());
        }

        log.debug("Synchronizing campaign with ID: {}", campaign.getId());
        campaignRepository.save(campaign);
        log.info("Successfully synchronized campaign with ID: {}", campaign.getId());
    }
}
