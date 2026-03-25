package com.crm.sync.worker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * DTO representing a campaign message received from RabbitMQ in the CRM worker.
 **/
@Data
@Builder
@ToString
public class CampaignMessage {
    private String campaignId;
    private String subCampaignId;
    private List<AttendeeDto> attendees;
}
