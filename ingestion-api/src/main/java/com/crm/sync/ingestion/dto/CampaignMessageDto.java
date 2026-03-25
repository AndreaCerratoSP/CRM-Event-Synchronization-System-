package com.crm.sync.ingestion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * DTO representing a campaign message to be sent to RabbitMQ in the CRM ingestion API.
 **/
@Data
@Builder
@ToString
public class CampaignMessageDto {
    @NotBlank
    private String campaignId;

    private String subCampaignId; //Nullable

    @NotEmpty
    private List<AttendeeDto> attendees;
}
