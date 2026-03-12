package com.ens.iam.adapter.inbound.rest.controller;

import com.ens.iam.adapter.inbound.rest.dto.request.LoginRequest;
import com.ens.iam.adapter.inbound.rest.mapper.AuthenticationRestMapper;
import com.ens.iam.application.port.in.AuthUseCase;
import com.ens.iam.application.port.in.result.TokenResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthUseCase authUseCase;
    private final AuthenticationRestMapper authenticationRestMapper;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResult login(@Valid @RequestBody LoginRequest request) {
        var loginCommand = authenticationRestMapper.toCommand(request);

        return authUseCase.login(loginCommand);
    }

}
