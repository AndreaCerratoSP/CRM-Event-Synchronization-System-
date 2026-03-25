package com.crm.sync.worker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(name = "campaign_id")
    private String campaignId;

    @Column(name = "sub_campaign_id")
    private String subCampaignId; // Nullable

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendees = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        lastUpdateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdateDate = LocalDateTime.now();
    }

    public void addAttendee(Attendee attendee){
        if(attendees == null)
            attendees = new ArrayList<>();

        attendee.setCampaign(this);
        attendees.add(attendee);
    }

    public void removeAttendee(Attendee attendee){
        if(attendees != null) {
            attendee.setCampaign(null);
            attendees.remove(attendee);
        }
    }

    public void clearAttendees() {
        if(attendees != null) {
            new ArrayList<>(attendees).forEach(this::removeAttendee);
        }
    }

    public void addAttendees(List<Attendee> attendees) {
        if(attendees != null) {
            for (Attendee att : new ArrayList<>(attendees)) {
                addAttendee(att);
            }
        }
    }

}
