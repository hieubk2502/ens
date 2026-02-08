package com.ens.iam.dto.admin;

import java.util.List;
import jakarta.validation.constraints.NotBlank;

public class CreateClientRequest {
    @NotBlank
    private String clientId;
    private String name;
    private Boolean publicClient = Boolean.TRUE;
    private Boolean standardFlowEnabled = Boolean.TRUE;
    private Boolean directAccessGrantsEnabled = Boolean.TRUE;
    private Boolean serviceAccountsEnabled = Boolean.FALSE;
    private List<String> redirectUris;
    private List<String> webOrigins;
    private String realm;
    private Boolean includeSecret = Boolean.FALSE;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublicClient() {
        return publicClient;
    }

    public void setPublicClient(Boolean publicClient) {
        this.publicClient = publicClient;
    }

    public Boolean getStandardFlowEnabled() {
        return standardFlowEnabled;
    }

    public void setStandardFlowEnabled(Boolean standardFlowEnabled) {
        this.standardFlowEnabled = standardFlowEnabled;
    }

    public Boolean getDirectAccessGrantsEnabled() {
        return directAccessGrantsEnabled;
    }

    public void setDirectAccessGrantsEnabled(Boolean directAccessGrantsEnabled) {
        this.directAccessGrantsEnabled = directAccessGrantsEnabled;
    }

    public Boolean getServiceAccountsEnabled() {
        return serviceAccountsEnabled;
    }

    public void setServiceAccountsEnabled(Boolean serviceAccountsEnabled) {
        this.serviceAccountsEnabled = serviceAccountsEnabled;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public List<String> getWebOrigins() {
        return webOrigins;
    }

    public void setWebOrigins(List<String> webOrigins) {
        this.webOrigins = webOrigins;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public Boolean getIncludeSecret() {
        return includeSecret;
    }

    public void setIncludeSecret(Boolean includeSecret) {
        this.includeSecret = includeSecret;
    }
}
