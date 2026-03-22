package com.crm.sync.ingestion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

/**
 * DTO representing an attendee in the CRM ingestion API.
 **/
@Data
@Builder
@ToString
public class AttendeeDto {
    private String cn; // Nullable
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    private LocalDate birthDate; // Nullable, "yyyy-MM-dd"
    
    @NotBlank
    private String partnerId;
    
    @NotNull
    private Boolean isCompanion;
    
    @NotBlank
    private String qrCode;
}
