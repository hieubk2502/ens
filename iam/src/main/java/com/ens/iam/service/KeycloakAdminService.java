package com.ens.iam.service;

import com.ens.iam.config.KeycloakProperties;
import com.ens.iam.dto.admin.CreateClientRequest;
import com.ens.iam.dto.admin.CreateClientResponse;
import com.ens.iam.dto.admin.CreateUserRequest;
import com.ens.iam.dto.auth.TokenResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KeycloakAdminService {
    private final RestClient restClient;
    private final KeycloakProperties properties;

    public KeycloakAdminService(RestClient keycloakRestClient, KeycloakProperties properties) {
        this.restClient = keycloakRestClient;
        this.properties = properties;
    }

    public CreateClientResponse createClient(CreateClientRequest request) {
        String realm = request.getRealm() != null ? request.getRealm() : properties.getRealm();
        String token = getAdminToken();

        Map<String, Object> payload = new HashMap<>();
        payload.put("clientId", request.getClientId());
        payload.put("name", request.getName());
        payload.put("publicClient", request.getPublicClient());
        payload.put("standardFlowEnabled", request.getStandardFlowEnabled());
        payload.put("directAccessGrantsEnabled", request.getDirectAccessGrantsEnabled());
        payload.put("serviceAccountsEnabled", request.getServiceAccountsEnabled());
        if (request.getRedirectUris() != null) {
            payload.put("redirectUris", request.getRedirectUris());
        }
        if (request.getWebOrigins() != null) {
            payload.put("webOrigins", request.getWebOrigins());
        }

        restClient.post()
                .uri("/admin/realms/{realm}/clients", realm)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .retrieve()
                .toBodilessEntity();

        String id = findClientIdByClientId(realm, request.getClientId(), token);
        String secret = null;
        if (Boolean.FALSE.equals(request.getPublicClient()) && Boolean.TRUE.equals(request.getIncludeSecret()) && id != null) {
            secret = getClientSecret(realm, id, token);
        }
        return new CreateClientResponse(id, request.getClientId(), secret);
    }

    public String createUser(CreateUserRequest request) {
        String realm = request.getRealm() != null ? request.getRealm() : properties.getRealm();
        String token = getAdminToken();

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", request.getUsername());
        payload.put("email", request.getEmail());
        payload.put("firstName", request.getFirstName());
        payload.put("lastName", request.getLastName());
        payload.put("enabled", request.getEnabled());
        if (request.getPassword() != null) {
            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", request.getPassword());
            credential.put("temporary", false);
            payload.put("credentials", List.of(credential));
        }

        URI location = restClient.post()
                .uri("/admin/realms/{realm}/users", realm)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .retrieve()
                .toBodilessEntity()
                .getHeaders()
                .getLocation();
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path != null ? path.substring(path.lastIndexOf('/') + 1) : null;
    }

    private String findClientIdByClientId(String realm, String clientId, String token) {
        List<Map<String, Object>> clients = restClient.get()
                .uri("/admin/realms/{realm}/clients?clientId={clientId}", realm, clientId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(List.class);
        if (clients == null || clients.isEmpty()) {
            return null;
        }
        Object id = clients.get(0).get("id");
        return id != null ? id.toString() : null;
    }

    private String getClientSecret(String realm, String id, String token) {
        Map<String, Object> response = restClient.get()
                .uri("/admin/realms/{realm}/clients/{id}/client-secret", realm, id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Map.class);
        if (response == null) {
            return null;
        }
        Object value = response.get("value");
        return value != null ? value.toString() : null;
    }

    private String getAdminToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", properties.getAdminClientId());
        form.add("username", properties.getAdminUsername());
        form.add("password", properties.getAdminPassword());

        TokenResponse tokenResponse = restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", properties.getAdminRealm())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(TokenResponse.class);
        return tokenResponse != null ? tokenResponse.getAccessToken() : null;
    }
}
