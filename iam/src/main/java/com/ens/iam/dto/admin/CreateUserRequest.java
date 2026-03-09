package com.ens.iam.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled = Boolean.TRUE;
    private String password;
    private String realm;
}
