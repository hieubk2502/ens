package com.ens.iam.application.port.in.command;

import java.util.List;

public record CreateClientCommand(
        String clientId,
        String name,
        Boolean publicClient,
        Boolean standardFlowEnabled,
        Boolean directAccessGrantsEnabled,
        Boolean serviceAccountsEnabled,
        List<String> redirectUris,
        List<String> webOrigins,
        String realm,
        Boolean includeSecret
) {}

