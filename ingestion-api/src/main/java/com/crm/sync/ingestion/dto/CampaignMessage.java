package com.crm.sync.ingestion.dto;

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
public class CampaignMessage {
    private String campaignId;
    private String subCampaignId;
    private List<AttendeeDto> attendees;
}
