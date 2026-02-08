package com.audit.hrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuditLogEvent(
        Map<String, Object> before,
        Map<String, Object> after,
        String op,
        @JsonProperty("ts_ms") Long tsMs,
        Source source
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Source(
            String db,
            @JsonProperty("ts_ms") Long tsMs,
            String schema,
            String table,
            Long lsn,
            @JsonProperty("txId") Long txId
    ) {}
}
