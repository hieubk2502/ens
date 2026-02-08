package com.ens.iam.service;

import com.ens.iam.config.KeycloakProperties;
import com.ens.iam.dto.auth.IntrospectRequest;
import com.ens.iam.dto.auth.LoginRequest;
import com.ens.iam.dto.auth.RefreshRequest;
import com.ens.iam.dto.auth.TokenResponse;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KeycloakAuthService {
    private final RestClient restClient;
    private final KeycloakProperties properties;

    public KeycloakAuthService(RestClient keycloakRestClient, KeycloakProperties properties) {
        this.restClient = keycloakRestClient;
        this.properties = properties;
    }

    public TokenResponse login(LoginRequest request) {
        String realm = request.getRealm() != null ? request.getRealm() : properties.getRealm();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", request.getClientId());
        if (request.getClientSecret() != null) {
            form.add("client_secret", request.getClientSecret());
        }
        form.add("username", request.getUsername());
        form.add("password", request.getPassword());

        return restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(TokenResponse.class);
    }

    public TokenResponse refresh(RefreshRequest request) {
        String realm = request.getRealm() != null ? request.getRealm() : properties.getRealm();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", request.getClientId());
        if (request.getClientSecret() != null) {
            form.add("client_secret", request.getClientSecret());
        }
        form.add("refresh_token", request.getRefreshToken());

        return restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(TokenResponse.class);
    }

    public Map<String, Object> introspect(IntrospectRequest request) {
        String realm = request.getRealm() != null ? request.getRealm() : properties.getRealm();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", request.getClientId());
        if (request.getClientSecret() != null) {
            form.add("client_secret", request.getClientSecret());
        }
        form.add("token", request.getToken());

        return restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token/introspect", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);
    }
}
