package com.ens.iam.application.service;

import com.ens.iam.application.port.in.AuthUseCase;
import com.ens.iam.application.port.in.command.IntrospectTokenCommand;
import com.ens.iam.application.port.in.command.authentication.LoginCommand;
import com.ens.iam.application.port.in.command.RefreshTokenCommand;
import com.ens.iam.application.port.in.result.TokenResult;
import com.ens.iam.application.port.out.IdentityProviderAuthPort;
import com.ens.iam.application.mapper.AuthenticationDomainMapper;
import java.util.Map;

import com.ens.iam.domain.model.authentication.AuthLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {
    private final IdentityProviderAuthPort identityProviderAuthPort;
    private final AuthenticationDomainMapper authenticationDomainMapper;

    @Override
    public TokenResult login(LoginCommand command) {

        AuthLogin authLogin = authenticationDomainMapper.toDomain(command);

        return identityProviderAuthPort.login(authLogin);
    }

    @Override
    public TokenResult refresh(RefreshTokenCommand command) {
        return identityProviderAuthPort.refresh(command);
    }

    @Override
    public Map<String, Object> introspect(IntrospectTokenCommand command) {
        return identityProviderAuthPort.introspect(command);
    }
}
