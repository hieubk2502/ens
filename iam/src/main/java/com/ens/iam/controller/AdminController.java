package com.ens.iam.controller;

import com.ens.iam.dto.admin.CreateClientRequest;
import com.ens.iam.dto.admin.CreateClientResponse;
import com.ens.iam.dto.admin.CreateUserRequest;
import com.ens.iam.service.KeycloakAdminService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iam/admin")
public class AdminController {
    private final KeycloakAdminService adminService;

    public AdminController(KeycloakAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateClientResponse createClient(@Valid @RequestBody CreateClientRequest request) {
        return adminService.createClient(request);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createUser(@Valid @RequestBody CreateUserRequest request) {
        String id = adminService.createUser(request);
        return Map.of("id", id);
    }
}
