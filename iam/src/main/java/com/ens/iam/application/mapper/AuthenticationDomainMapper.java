package com.ens.iam.application.mapper;

import com.ens.iam.application.port.in.command.authentication.LoginCommand;
import com.ens.iam.domain.model.authentication.AuthLogin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationDomainMapper {
    AuthLogin toDomain(LoginCommand command);
}

