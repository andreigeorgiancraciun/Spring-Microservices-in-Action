package com.optimagrowth.license.event.handler;

import com.optimagrowth.license.event.model.OrganizationChangeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final String TOPIC_NAME = "orgChangeTopic";
    private static final String LICENSING_GROUP = "licensingGroup";

    @KafkaListener(topics = TOPIC_NAME, groupId = LICENSING_GROUP)
    public void loggerSink(OrganizationChangeModel organization) {
        logger.debug("Received a message of type {}", organization.getType());

        switch (organization.getAction()) {
            case "GET":
                logger.debug("Received a GET event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "SAVE":
                logger.debug("Received a SAVE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "UPDATE":
                logger.debug("Received a UPDATE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            default:
                logger.error("Received an UNKNOWN event from the organization service of type {}", organization.getType());
                break;
        }
    }
}
