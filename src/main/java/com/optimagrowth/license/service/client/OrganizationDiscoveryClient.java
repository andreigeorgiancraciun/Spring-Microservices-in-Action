package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.service.utils.UserContextFilter;
import com.optimagrowth.license.service.utils.UserContextHolder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OrganizationDiscoveryClient {
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    public OrganizationDiscoveryClient(DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    public Organization getOrganization(String organizationId, String authorization) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(UserContextFilter.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization);
        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/v1/organization/%s", instances.get(0).getUri().toString(), organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        httpEntity,
                        Organization.class,
                        organizationId);

        return restExchange.getBody();
    }
}
