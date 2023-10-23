package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.redis.OrganizationRedisRepository;
import com.optimagrowth.license.utils.UserContextFilter;
import com.optimagrowth.license.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    private final RestTemplate restTemplate;
    private final OrganizationRedisRepository redisRepository;

    public OrganizationRestTemplateClient(RestTemplate restTemplate, OrganizationRedisRepository redisRepository) {
        this.restTemplate = restTemplate;
        this.redisRepository = redisRepository;
    }

    public Organization getOrganization(String organizationId, String authorization) {
        logger.debug("In Licensing Service.getOrganization: {}", UserContextHolder.getContext().getCorrelationId());

        Organization organization = checkRedisCache(organizationId);

        if (organization != null) {
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }

        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(UserContextFilter.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://organization-service/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        httpEntity,
                        Organization.class,
                        organizationId);

        /*Save the record from cache*/
        organization = restExchange.getBody();

        if (organization != null) {
            cacheOrganizationObject(organization);
        }

        return organization;
    }

    private Organization checkRedisCache(String organizationId) {
        try {
            return redisRepository.findById(organizationId).orElse(null);
        } catch (Exception ex) {
            logger.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex.getMessage());
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            redisRepository.save(organization);
        } catch (Exception ex) {
            logger.error("Unable to cache organization {} in Redis. Exception {}", organization.getId(), ex.getMessage());
        }
    }

}
