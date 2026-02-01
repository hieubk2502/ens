package com.ens.hrm.controller.organization;


import com.ens.hrm.dto.organization.request.HrmOrganizationRequest;
import com.ens.hrm.dto.organization.response.HrmOrganizationResponse;
import com.ens.hrm.service.organization.HrmOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class HrmOrganizationController {

    private final HrmOrganizationService organizationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HrmOrganizationResponse create(@RequestBody HrmOrganizationRequest request) {
        return organizationService.create(request);
    }
}
