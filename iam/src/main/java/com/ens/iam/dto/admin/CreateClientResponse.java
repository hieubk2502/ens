package com.ens.iam.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClientResponse {
    private String id;
    private String clientId;
    private String secret;
}
