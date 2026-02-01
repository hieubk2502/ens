package com.ens.hrm.service.organization;

import com.ens.hrm.dto.organization.request.HrmOrganizationRequest;
import com.ens.hrm.dto.organization.response.HrmOrganizationResponse;
import com.ens.hrm.entity.HrmOrganization;
import com.ens.hrm.repository.organization.HrmOrganizationRepo;
import com.ens.hrm.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HrmOrganizationServiceImpl implements HrmOrganizationService {

    private final HrmOrganizationRepo hrmOrganizationRepo;

    @Override
    public HrmOrganizationResponse create(HrmOrganizationRequest request) {
        HrmOrganization create = HrmOrganization.builder()
                .code(RandomUtil.randomAlpha(5))
                .name(RandomUtil.randomAlpha(6))
                .build();
        log.info(create.toString());
        hrmOrganizationRepo.save(create);
        return null;
    }
}
