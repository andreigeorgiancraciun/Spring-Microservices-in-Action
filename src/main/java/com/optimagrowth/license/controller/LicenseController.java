package com.optimagrowth.license.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping(value = "/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId,
                                              @RequestHeader("Authorization") String authorization) {

        License license = licenseService.getLicense(licenseId, organizationId, "", authorization);
        return ResponseEntity.ok(license);
    }

    @GetMapping(value = "/{licenseId}/{clientType}")
    public ResponseEntity<License> getLicensesWithClient(@PathVariable("organizationId") String organizationId,
                                                         @PathVariable("licenseId") String licenseId,
                                                         @PathVariable("clientType") String clientType,
                                                         @RequestHeader("Authorization") String authorization) {

        License license = licenseService.getLicense(licenseId, organizationId, clientType, authorization);
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.createLicense(request));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId, @PathVariable String organizationId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
    }

    @GetMapping
    public ResponseEntity<List<License>> getLicenses(@PathVariable("organizationId") String organizationId) throws TimeoutException {
        List<License> licenses = licenseService.getLicensesByOrganization(organizationId);
        return ResponseEntity.ok(licenses);
    }
}