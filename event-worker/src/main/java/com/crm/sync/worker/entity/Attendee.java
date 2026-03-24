package com.crm.sync.worker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing an attendee of a campaign.
 */
@Entity
@Data
@Table(name = "attendee")
@ToString(exclude = "campaign")
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String cn; // Nullable
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    private LocalDate birthDate; // Nullable
    
    @Column(nullable = false)
    private String partnerId;
    
    @Column(nullable = false)
    private Boolean isCompanion;
    
    @Column(nullable = false, unique = true)
    private String qrCode;
    
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        lastUpdateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdateDate = LocalDateTime.now();
    }
}
