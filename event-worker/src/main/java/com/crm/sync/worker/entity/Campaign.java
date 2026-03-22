package com.crm.sync.worker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Entity representing a campaign, which may have multiple attendees.
 */
@Entity
@Data
@Table(name = "campaign")
@ToString(exclude = "attendees")
public class Campaign {
    @Id
    private String id; // campaignId
    
    private String subCampaignId; // Nullable
    
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendees;
}
