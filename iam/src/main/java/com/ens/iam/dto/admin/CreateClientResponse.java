package com.ens.iam.dto.admin;

public class CreateClientResponse {
    private String id;
    private String clientId;
    private String secret;

    public CreateClientResponse() {}

    public CreateClientResponse(String id, String clientId, String secret) {
        this.id = id;
        this.clientId = clientId;
        this.secret = secret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
