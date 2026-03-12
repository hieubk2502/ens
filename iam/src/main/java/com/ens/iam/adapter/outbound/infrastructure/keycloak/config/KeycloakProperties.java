package com.ens.iam.adapter.outbound.infrastructure.keycloak.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String baseUrl;

    private String realm;

    private String clientId;

    private String clientSecret;

}
