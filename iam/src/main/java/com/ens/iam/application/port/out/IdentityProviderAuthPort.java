package com.ens.iam.application.port.out;

import com.ens.iam.domain.model.authentication.AuthLogin;
import com.ens.iam.application.port.in.command.IntrospectTokenCommand;
import com.ens.iam.application.port.in.command.RefreshTokenCommand;
import com.ens.iam.application.port.in.result.TokenResult;
import java.util.Map;

public interface IdentityProviderAuthPort {
    TokenResult login(AuthLogin login);

    TokenResult refresh(RefreshTokenCommand command);

    Map<String, Object> introspect(IntrospectTokenCommand command);
}
