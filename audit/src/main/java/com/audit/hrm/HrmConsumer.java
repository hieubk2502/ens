package com.audit.organization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrganizationConsumer {

    @KafkaListener(topics = "ens.hrm.hrm_organization")
    public void audit(String message) {
        log.info("Message: " + message);
    }
}
