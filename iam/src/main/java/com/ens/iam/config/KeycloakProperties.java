package com.ens.iam.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String baseUrl ;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String adminRealm;
    private String adminClientId;
    private String adminUsername;
    private String adminPassword;
}
