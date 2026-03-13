package com.ens.iam.application.port.in;

import com.ens.iam.application.port.in.command.CreateClientCommand;
import com.ens.iam.application.port.in.command.CreateUserCommand;
import com.ens.iam.application.port.in.result.CreateClientResult;

public interface AdminUseCase {
    CreateClientResult createClient(CreateClientCommand command);

    String createUser(CreateUserCommand command);
}

