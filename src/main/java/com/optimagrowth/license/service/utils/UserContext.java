package com.optimagrowth.license.service.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserContext {

    private String correlationId = "";
    private String authToken = "";
    private String userId = "";
    private String organizationId = "";

}
