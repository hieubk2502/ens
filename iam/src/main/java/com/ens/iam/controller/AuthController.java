package com.ens.iam.controller;

import com.ens.iam.dto.auth.IntrospectRequest;
import com.ens.iam.dto.auth.LoginRequest;
import com.ens.iam.dto.auth.RefreshRequest;
import com.ens.iam.dto.auth.TokenResponse;
import com.ens.iam.service.KeycloakAuthService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iam/auth")
public class AuthController {
    private final KeycloakAuthService authService;

    public AuthController(KeycloakAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/introspect")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> introspect(@Valid @RequestBody IntrospectRequest request) {
        return authService.introspect(request);
    }
}
