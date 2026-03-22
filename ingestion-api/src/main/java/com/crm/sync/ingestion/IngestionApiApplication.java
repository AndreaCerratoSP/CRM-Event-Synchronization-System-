package com.crm.sync.ingestion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the CRM ingestion API.
 **/
@SpringBootApplication
@Slf4j
public class IngestionApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(IngestionApiApplication.class, args);
        log.info("CRM ingestion API started successfully.");
    }
}
