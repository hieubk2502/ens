package com.ens.iam.application.port.in.result;

public record TokenResult(
        String accessToken,
        String refreshToken,
        Long expiresIn,
        Long refreshExpiresIn,
        String tokenType,
        String scope
) {}

