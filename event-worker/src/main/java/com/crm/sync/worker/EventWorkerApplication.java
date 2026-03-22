package com.crm.sync.worker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the CRM event worker.
 * This application is responsible for processing events from the message queue and performing necessary actions.
 */
@SpringBootApplication
@Slf4j
public class EventWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventWorkerApplication.class, args);
        log.info("CRM Event Worker started successfully.");
    }
}
