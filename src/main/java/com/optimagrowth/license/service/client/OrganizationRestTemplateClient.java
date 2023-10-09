package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.service.utils.UserContextFilter;
import com.optimagrowth.license.service.utils.UserContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    private final RestTemplate restTemplate;

    public OrganizationRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Organization getOrganization(String organizationId, String authorization) {
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

        return restExchange.getBody();
    }
}
