package com.crm.sync.worker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Entity representing a campaign, which may have multiple attendees.
 */
@Entity
@Data
@Table(name = "campaign")
@ToString(exclude = "attendees")
public class Campaign {
    @Id
    private String campagnId; // campaignId
    
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

    public void removeAttendees(List<Attendee> newAttendees) {
        if(newAttendees == null)
            newAttendees = new ArrayList<>();

        if (attendees != null){
            Map<String, Attendee> updatedMap = newAttendees.stream()
                    .collect(Collectors.toMap(Attendee::getQrCode, Function.identity()));

            for (Attendee att : attendees) {
                Attendee attendee = updatedMap.get(att.getQrCode());
                if (attendee == null)
                    removeAttendee(att);
            }
        }
    }

    public void addAttendees(List<Attendee> newAttendees) {
        List<Attendee> updatedAttendees = new ArrayList<>();

        if (newAttendees != null){
            if(attendees == null)
                attendees = new ArrayList<>();

            Map<String, Attendee> existingMap = attendees.stream()
                    .collect(Collectors.toMap(Attendee::getQrCode, Function.identity()));

            for (Attendee att : newAttendees) {
                Attendee attendee = existingMap.get(att.getQrCode());
                if (attendee != null) {
                    // update valuew
                    merge(att, attendee);
                    updatedAttendees.add(attendee);
                    existingMap.remove(att.getQrCode());
                } else {
                    // new attendee
                    Attendee newAttendee = new Attendee();
                    newAttendee.setQrCode(att.getQrCode());
                    merge(att, newAttendee);
                    newAttendee.setCampaign(this);
                    updatedAttendees.add(newAttendee);
                }
            }
        }

        this.setAttendees(updatedAttendees);
    }

    private void merge(Attendee source, Attendee dest){
        dest.setCn(source.getCn());
        dest.setFirstName(source.getFirstName());
        dest.setLastName(source.getLastName());
        dest.setBirthDate(source.getBirthDate());
        dest.setPartnerId(source.getPartnerId());
        dest.setIsCompanion(source.getIsCompanion());
    }
}
