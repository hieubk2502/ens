package com.ens.iam.application.port.in.command;

public record RefreshTokenCommand(String refreshToken, String realm) {}

