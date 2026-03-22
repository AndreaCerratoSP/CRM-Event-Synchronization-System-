package com.crm.sync.ingestion.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * DTO representing a campaign request received from the client in the CRM ingestion API.
 **/
@Data
@ToString
public class CampaignRequest {
    @NotBlank
    private String campaignId;
    
    private String subCampaignId; // Nullable
    
    @NotEmpty
    private List<@Valid AttendeeDto> attendees;
}
