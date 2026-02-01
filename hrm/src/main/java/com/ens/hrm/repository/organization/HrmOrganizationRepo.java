package com.ens.hrm.repository.organization;

import com.ens.hrm.entity.HrmOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrmOrganizationRepo extends JpaRepository<HrmOrganization, Long> {
}
