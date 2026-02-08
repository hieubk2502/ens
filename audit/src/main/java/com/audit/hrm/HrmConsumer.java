package com.audit.hrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HrmConsumer {

    ObjectMapper objectMapper;
    AuditBatchService batchService;

    @KafkaListener(topics = "ens.hrm.hrm_organization")
    public void audit(DynamicMessage message) {
        try {
            String json = JsonFormat.printer().omittingInsignificantWhitespace().print(message);
            AuditLogEvent event = objectMapper.readValue(json, AuditLogEvent.class);
            AuditBatchService.AuditLogRow row = batchService.toRow(event);
            batchService.add(row, batchService.estimateBytes(json));
        } catch (Exception ex) {
            log.warn("Message (raw): {}", message, ex);
        }
    }
}
