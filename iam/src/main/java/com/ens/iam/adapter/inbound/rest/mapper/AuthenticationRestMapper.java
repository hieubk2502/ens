package com.ens.iam.adapter.inbound.rest.mapper;

import com.ens.iam.adapter.inbound.rest.dto.request.LoginRequest;
import com.ens.iam.application.port.in.command.authentication.LoginCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationRestMapper {
    LoginCommand toCommand(LoginRequest request);
}

