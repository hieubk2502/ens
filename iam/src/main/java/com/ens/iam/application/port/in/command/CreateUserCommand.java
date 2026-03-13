package com.ens.iam.application.port.in.command;

public record CreateUserCommand(
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean enabled,
        String password,
        String realm
) {}

