package com.crm.sync.worker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 
 * Configuration class for RabbitMQ integration in the CRM event worker.
 **/
@Configuration
@Slf4j
public class RabbitMQConfig {

	@Value("${crm.api.key}")
    public static String crmCampaignsQueue;

	/**
	 * Defines a RabbitMQ queue for CRM campaigns.
	 **/
    @Bean
    public Queue campaignsQueue() {
        return new Queue(crmCampaignsQueue, true);
    }

    /**
	 * Configures a message converter to use JSON for message serialization and deserialization.
	 * This allows messages to be sent and received in JSON format, making it easier to work with complex data structures.
	 *
	 * @return a MessageConverter that uses Jackson for JSON conversion
	 **/
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

