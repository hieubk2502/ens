package com.ens.hrm.service.organization;

import com.ens.hrm.dto.organization.request.HrmOrganizationRequest;
import com.ens.hrm.dto.organization.response.HrmOrganizationResponse;

public interface HrmOrganizationService {
    HrmOrganizationResponse create(HrmOrganizationRequest request);
}
