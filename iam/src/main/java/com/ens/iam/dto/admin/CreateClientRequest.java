package com.ens.iam.dto.admin;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClientRequest {
    @NotBlank
    private String clientId;
    private String name;
    private Boolean publicClient = Boolean.TRUE;
    private Boolean standardFlowEnabled = Boolean.TRUE;
    private Boolean directAccessGrantsEnabled = Boolean.TRUE;
    private Boolean serviceAccountsEnabled = Boolean.FALSE;
    private List<String> redirectUris;
    private List<String> webOrigins;
    private String realm;
    private Boolean includeSecret = Boolean.FALSE;
}
