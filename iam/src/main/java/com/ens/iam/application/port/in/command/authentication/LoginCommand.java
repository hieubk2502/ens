package com.ens.iam.application.port.in.command.authentication;

public record LoginCommand(String username, String password, String clientId) {}
