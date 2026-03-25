package com.crm.sync.worker.service;

import com.crm.sync.worker.dto.AttendeeDto;
import com.crm.sync.worker.dto.CampaignMessageDto;
import com.crm.sync.worker.entity.Attendee;
import com.crm.sync.worker.entity.Campaign;
import com.crm.sync.worker.mapper.AttendeeMapper;
import com.crm.sync.worker.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the SyncService interface for synchronizing campaign data.
 * This service handles the logic for saving or updating campaign information in the database.
 */
@Service
@Slf4j
public class SyncServiceImpl implements SyncService {

    private CampaignRepository campaignRepository;

    private AttendeeMapper attendeeMapper;

    public SyncServiceImpl(CampaignRepository campaignRepository, AttendeeMapper attendeeMapper){
        this.attendeeMapper = attendeeMapper;
        this.campaignRepository = campaignRepository;
    }


    /**
	 * Synchronizes the given campaign data with the database.
	 * If the campaign already exists, it will be updated; otherwise, a new record will be created.
	 *
	 * @param campaignMessage The campaign to be synchronized.
	 */
    @Override
    @Transactional
    public void synchronizeCampaign(CampaignMessageDto campaignMessage) {

        log.info("Synchronizing campaign with ID: {}", campaignMessage.getCampaignId());

        if (campaignMessage == null || campaignMessage.getCampaignId() == null) {
            log.warn("Received null campaign or campaign with null ID, skipping synchronization");
            return;
        }

        // Try to retrieve the Campaign from the database using the ID from the message.
        // If it does not exist, create a new Campaign instance.
        Campaign campaign = campaignRepository.findByCampaignId(campaignMessage.getCampaignId());

        if(campaign == null) {
            campaign = new Campaign();
            log.info("New campaign ");
        }
        else
            log.info("Update campaign with id {}", campaign.getCampaignId());

        // Set the values from the incoming message.
        campaign.setCampaignId(campaignMessage.getCampaignId());
        campaign.setSubCampaignId(campaignMessage.getSubCampaignId());

        List<Attendee> newAttendees = attendeeMapper.toEntity(campaignMessage.getAttendees());
        log.info("{} new attendees found in message", newAttendees != null ? newAttendees.size() : 0);

        // Remove deleted attendees
        campaign.clearAttendees();

        // Add the new Attendees to Campaign
        campaign.addAttendees(newAttendees);

        // Save the Camapign
        campaignRepository.save(campaign);
        log.info("Successfully synchronized campaign with ID: {}", campaign.getCampaignId());
    }

}
