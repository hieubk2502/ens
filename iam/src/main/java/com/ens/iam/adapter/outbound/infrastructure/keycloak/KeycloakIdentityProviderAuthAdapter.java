package com.ens.iam.adapter.outbound.infrastructure.keycloak;

import com.ens.iam.adapter.outbound.infrastructure.keycloak.config.KeycloakProperties;
import com.ens.iam.adapter.outbound.infrastructure.keycloak.dto.KeycloakTokenResponse;
import com.ens.iam.application.port.in.command.IntrospectTokenCommand;
import com.ens.iam.application.port.in.command.RefreshTokenCommand;
import com.ens.iam.application.port.in.result.TokenResult;
import com.ens.iam.application.port.out.IdentityProviderAuthPort;
import com.ens.iam.domain.model.authentication.AuthLogin;

import java.net.URI;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KeycloakIdentityProviderAuthAdapter implements IdentityProviderAuthPort {
    private final RestClient restClient;
    private final KeycloakProperties properties;

    @Override
    public TokenResult login(AuthLogin request) {
        String realm = properties.getRealm();

        String username = request.username();
        String password = request.password();
        String clientId = request.clientId();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD);
        form.add(OAuth2Constants.CLIENT_ID, clientId);
        form.add(OAuth2Constants.CLIENT_SECRET, properties.getClientSecret());
        form.add(OAuth2Constants.USERNAME, username);
        form.add(OAuth2Constants.PASSWORD, password);

        URI uri = KeycloakUriBuilder.fromUri(properties.getBaseUrl())
                .path(properties.getUrl())
                .build(properties.getRealm());

        KeycloakTokenResponse token = restClient.post()
                .uri(uri.toString(), realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(KeycloakTokenResponse.class);

        return new TokenResult(
                token.getAccessToken(),
                token.getRefreshToken(),
                token.getExpiresIn(),
                token.getRefreshExpiresIn(),
                token.getTokenType(),
                token.getScope()
        );
    }

    @Override
    public TokenResult refresh(RefreshTokenCommand command) {
        String realm = command.realm() != null ? command.realm() : properties.getRealm();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", properties.getClientId());
        if (StringUtils.hasText(properties.getClientSecret())) {
            form.add("client_secret", properties.getClientSecret());
        }
        form.add("refresh_token", command.refreshToken());

        KeycloakTokenResponse token = restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(KeycloakTokenResponse.class);

        return new TokenResult(
                token.getAccessToken(),
                token.getRefreshToken(),
                token.getExpiresIn(),
                token.getRefreshExpiresIn(),
                token.getTokenType(),
                token.getScope()
        );
    }

    @Override
    public Map<String, Object> introspect(IntrospectTokenCommand command) {
        String realm = command.realm() != null ? command.realm() : properties.getRealm();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", properties.getClientId());
        if (StringUtils.hasText(properties.getClientSecret())) {
            form.add("client_secret", properties.getClientSecret());
        }
        form.add("token", command.token());

        return restClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token/introspect", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);
    }
}
