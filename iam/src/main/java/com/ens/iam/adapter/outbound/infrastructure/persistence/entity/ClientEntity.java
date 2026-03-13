package com.ens.iam.adapter.outbound.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "client",
        schema = "public",
        indexes = {@Index(name = "idx_client_id", columnList = "client_id")},
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_b71cjlbenv945rb6gcon438at", columnNames = {"realm_id", "client_id"})}
)
@Getter
@Setter
public class ClientEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "full_scope_allowed", nullable = false)
    private boolean fullScopeAllowed;

    @Column(name = "client_id", length = 255)
    private String clientId;

    @Column(name = "not_before")
    private Integer notBefore;

    @Column(name = "public_client", nullable = false)
    private boolean publicClient;

    @Column(name = "secret", length = 255)
    private String secret;

    @Column(name = "base_url", length = 255)
    private String baseUrl;

    @Column(name = "bearer_only", nullable = false)
    private boolean bearerOnly;

    @Column(name = "management_url", length = 255)
    private String managementUrl;

    @Column(name = "surrogate_auth_required", nullable = false)
    private boolean surrogateAuthRequired;

    @Column(name = "realm_id", length = 36)
    private String realmId;

    @Column(name = "protocol", length = 255)
    private String protocol;

    @Column(name = "node_rereg_timeout")
    private Integer nodeReregTimeout;

    @Column(name = "frontchannel_logout", nullable = false)
    private boolean frontchannelLogout;

    @Column(name = "consent_required", nullable = false)
    private boolean consentRequired;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "service_accounts_enabled", nullable = false)
    private boolean serviceAccountsEnabled;

    @Column(name = "client_authenticator_type", length = 255)
    private String clientAuthenticatorType;

    @Column(name = "root_url", length = 255)
    private String rootUrl;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "registration_token", length = 255)
    private String registrationToken;

    @Column(name = "standard_flow_enabled", nullable = false)
    private boolean standardFlowEnabled;

    @Column(name = "implicit_flow_enabled", nullable = false)
    private boolean implicitFlowEnabled;

    @Column(name = "direct_access_grants_enabled", nullable = false)
    private boolean directAccessGrantsEnabled;

    @Column(name = "always_display_in_console", nullable = false)
    private boolean alwaysDisplayInConsole;
}
