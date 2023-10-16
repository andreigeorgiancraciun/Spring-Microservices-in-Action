package com.optimagrowth.license.event.handler;

import com.optimagrowth.license.event.model.OrganizationChangeModel;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.redis.OrganizationRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final String TOPIC_NAME = "orgChangeTopic";
    private static final String LICENSING_GROUP = "licensingGroup";
    private final OrganizationRedisRepository redisRepository;

    public KafkaConsumerService(OrganizationRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @KafkaListener(topics = TOPIC_NAME, groupId = LICENSING_GROUP)
    public void loggerSink(OrganizationChangeModel organizationChangeModel) {
        logger.debug("Received a message of type {}", organizationChangeModel.getType());

        Organization organization = organizationChangeModel.getOrganization();

        switch (organizationChangeModel.getAction()) {
            case "GET":
                logger.debug("Received a GET event from the organization service for organization id {}", organization.getId());
                break;
            case "SAVE":
                logger.debug("Received a SAVE event from the organization service for organization id {}", organization.getId());
                redisRepository.save(organization);
                break;
            case "UPDATE":
                logger.debug("Received a UPDATE event from the organization service for organization id {}", organization.getId());
                redisRepository.save(organization);
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the organization service for organization id {}", organization.getId());
                redisRepository.deleteById(organization.getId());
                break;
            default:
                logger.error("Received an UNKNOWN event from the organization service of type {}", organizationChangeModel.getType());
                break;
        }
    }
}
