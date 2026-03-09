package com.ens.iam.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class IntrospectRequest {
    @NotBlank
    private String token;
    private String realm;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
