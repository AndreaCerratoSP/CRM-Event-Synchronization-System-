package com.crm.sync.worker.dto;

import com.crm.sync.worker.dto.AttendeeDto;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * DTO representing a campaign message received from RabbitMQ in the CRM worker.
 **/
@Data
@Builder
@ToString
public class CampaignMessage implements Serializable {
    private String campaignId;
    private String subCampaignId;
    private List<AttendeeDto> attendees;
}
