package com.crm.sync.worker.service;

import com.crm.sync.worker.dto.CampaignMessage;
import com.crm.sync.worker.entity.Attendee;
import com.crm.sync.worker.entity.Campaign;
import com.crm.sync.worker.repository.AttendeeRepository;
import com.crm.sync.worker.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private AttendeeRepository attendeeRepository;

    /**
	 * Synchronizes the given campaign data with the database.
	 * If the campaign already exists, it will be updated; otherwise, a new record will be created.
	 *
	 * @param campaignMessage The campaign to be synchronized.
	 */
    @Override
    @Transactional
    public void synchronizeCampaign(CampaignMessage campaignMessage) {
        // 1. Prima controlla l'array (e la validità della campagna)
        if (campaignMessage == null || campaignMessage.getCampaignId() == null) {
            log.warn("Received null campaign or campaign with null ID, skipping synchronization");
            return;
        }

        // 2. Salva le campagne e gli atendee nuovi (quindi con id non trovato sul db)
        // Se l'id esiste già, aggiorniamo i dati senza sollevare eccezioni
        Campaign campaign = campaignRepository.findById(campaignMessage.getCampaignId())
                .orElse(new Campaign());
        
        if (campaign.getId() == null) {
            campaign.setId(campaignMessage.getCampaignId());
            log.debug("Creating new campaign with ID: {}", campaign.getId());
        } else {
            log.debug("Updating existing campaign with ID: {}", campaign.getId());
            campaign.setLastUpdateDate(LocalDateTime.now());
        }
        
        campaign.setSubCampaignId(campaignMessage.getSubCampaignId());
        
        // Salviamo subito la campagna per assicurarci che esista prima di gestire gli attendee
        campaign = campaignRepository.saveAndFlush(campaign);

        if (campaignMessage.getAttendees() != null && !campaignMessage.getAttendees().isEmpty()) {
            if (campaign.getAttendees() == null) {
                campaign.setAttendees(new ArrayList<>());
            }

            // 3. Elimina gli atendee che erano già presenti, aggiorna le informazioni e li aggiunge
            // Per ogni attendee nel messaggio, controlliamo se il QR code esiste già nel DB (anche su altre campagne)
            for (com.crm.sync.worker.dto.AttendeeDto dto : campaignMessage.getAttendees()) {
                if (dto.getQrCode() == null) continue;

                // Cerchiamo l'attendee globale per QR code
                Optional<Attendee> existingAttendee = attendeeRepository.findByQrCode(dto.getQrCode());
                
                if (existingAttendee.isPresent()) {
                    Attendee oldAttendee = existingAttendee.get();
                    // Se l'attendee esiste già, lo eliminiamo per poi reinserirlo aggiornato
                    log.debug("Found existing attendee with QR code: {}, deleting to update", dto.getQrCode());
                    
                    // Rimuoviamo il vecchio riferimento dalla sua campagna originale se necessario
                    Campaign oldCampaign = oldAttendee.getCampaign();
                    if (oldCampaign != null) {
                        oldCampaign.getAttendees().remove(oldAttendee);
                        campaignRepository.save(oldCampaign);
                    }
                    
                    attendeeRepository.delete(oldAttendee);
                    attendeeRepository.flush(); // Forza l'eliminazione per evitare conflitti di chiave UNIQUE
                }

                // Aggiungiamo il nuovo/aggiornato attendee alla campagna corrente
                Attendee attendee = new Attendee();
                attendee.setCn(dto.getCn());
                attendee.setFirstName(dto.getFirstName());
                attendee.setLastName(dto.getLastName());
                attendee.setBirthDate(dto.getBirthDate());
                attendee.setPartnerId(dto.getPartnerId());
                attendee.setIsCompanion(dto.getIsCompanion());
                attendee.setQrCode(dto.getQrCode());
                attendee.setCampaign(campaign);
                
                campaign.getAttendees().add(attendee);
            }
            
            log.debug("Processed and updated {} attendees for campaign ID: {}", campaignMessage.getAttendees().size(), campaign.getId());
        }

        log.debug("Finalizing synchronization for campaign ID: {}", campaign.getId());
        // Salviamo definitivamente tutti i cambiamenti (campagna e attendee associati) nel database
        campaignRepository.saveAndFlush(campaign);
        log.info("Successfully synchronized campaign with ID: {}", campaign.getId());
    }
}
