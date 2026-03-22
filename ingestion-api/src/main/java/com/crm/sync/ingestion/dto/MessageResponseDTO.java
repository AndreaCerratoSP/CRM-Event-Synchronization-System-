package com.crm.sync.ingestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a message response sent back to the client in the CRM worker.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {

	private String message;
    
}
