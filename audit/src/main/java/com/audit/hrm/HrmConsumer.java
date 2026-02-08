package com.audit.hrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HrmConsumer {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @KafkaListener(topics = "ens.hrm.hrm_organization")
    public void audit(DynamicMessage message) {
        try {
            String json = JsonFormat.printer().omittingInsignificantWhitespace().print(message);
            HrmEvent event = MAPPER.readValue(json, HrmEvent.class);
            log.info("Event op={}, db={}, schema={}, table={}, lsn={}",
                    event.op(),
                    event.source() != null ? event.source().db() : null,
                    event.source() != null ? event.source().schema() : null,
                    event.source() != null ? event.source().table() : null,
                    event.source() != null ? event.source().lsn() : null);
            log.info("Event data: before={}, after={}", event.before(), event.after());
        } catch (Exception ex) {
            log.warn("Message (raw): {}", message, ex);
        }
    }
}
