package com.optimagrowth.license.event.model;

import com.optimagrowth.license.model.Organization;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationChangeModel {
    private String type;
    private String action;
    private Organization organization;
    private String correlationId;
}
