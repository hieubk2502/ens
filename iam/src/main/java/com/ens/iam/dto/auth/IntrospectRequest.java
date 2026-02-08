package com.ens.iam.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class IntrospectRequest {
    @NotBlank
    private String token;
    @NotBlank
    private String clientId;
    private String clientSecret;
    private String realm;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
