package com.ens.iam.adapter.outbound.infrastructure.keycloak.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfig {


    @Bean
    public Keycloak keycloakRestClient(KeycloakProperties properties) {
        return KeycloakBuilder.builder()
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .serverUrl(properties.getBaseUrl())
                .resteasyClient(keycloakResteasyClient())
                .build();
    }

    @Bean
    public ResteasyClient keycloakResteasyClient() {
        return new ResteasyClientBuilder()
                .connectionPoolSize(100)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();
    }
}

