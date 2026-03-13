package com.ens.iam.application.port.in.command;

public record IntrospectTokenCommand(String token, String realm) {}

