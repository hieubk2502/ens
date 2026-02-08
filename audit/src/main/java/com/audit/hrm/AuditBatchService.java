package com.audit.hrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditBatchService {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    private final ReentrantLock lock = new ReentrantLock();
    private final List<AuditLogRow> buffer = new ArrayList<>();
    private long bufferBytes = 0L;

    @Value("${audit.batch.max-bytes:2097152}")
    private long maxBytes;

    @Scheduled(fixedDelayString = "${audit.batch.flush-interval-ms:20000}")
    public void flushScheduled() {
        flush(false);
    }

    public void add(AuditLogRow row, long approxBytes) {
        lock.lock();
        try {
            buffer.add(row);
            bufferBytes += approxBytes;
        } finally {
            lock.unlock();
        }
        if (bufferBytes >= maxBytes) {
            flush(true);
        }
    }

    public void flush(boolean sizeTriggered) {
        List<AuditLogRow> batch;
        lock.lock();
        try {
            if (buffer.isEmpty()) {
                return;
            }
            batch = new ArrayList<>(buffer);
            buffer.clear();
            bufferBytes = 0L;
        } finally {
            lock.unlock();
        }

        String sql = "insert into audit.audit_log"
                + " (db_name, schema_name, table_name, row_id, op, data, lsn, tx_id, event_ts)"
                + " values (?, ?, ?, ?, ?, ?::jsonb, ?, ?, ?)"
                + " on conflict (db_name, schema_name, table_name, lsn) do nothing";
        jdbcTemplate.batchUpdate(sql, batch, batch.size(), (ps, row) -> {
            ps.setString(1, row.dbName());
            ps.setString(2, row.schemaName());
            ps.setString(3, row.tableName());
            if (row.rowId() != null) {
                ps.setLong(4, row.rowId());
            } else {
                ps.setNull(4, java.sql.Types.BIGINT);
            }
            ps.setString(5, row.op());
            ps.setString(6, row.dataJson());
            if (row.lsn() != null) {
                ps.setLong(7, row.lsn());
            } else {
                ps.setNull(7, java.sql.Types.BIGINT);
            }
            if (row.txId() != null) {
                ps.setLong(8, row.txId());
            } else {
                ps.setNull(8, java.sql.Types.BIGINT);
            }
            if (row.eventTs() != null) {
                ps.setTimestamp(9, Timestamp.from(row.eventTs()));
            } else {
                ps.setNull(9, java.sql.Types.TIMESTAMP_WITH_TIMEZONE);
            }
        });
        log.info("Flushed {} audit rows (sizeTriggered={})", batch.size(), sizeTriggered);
    }

    public AuditLogRow toRow(AuditLogEvent event) {
        Map<String, Object> data = event.after() != null ? event.after() : event.before();
        String dataJson;
        try {
            dataJson = data != null ? objectMapper.writeValueAsString(data) : null;
        } catch (Exception ex) {
            dataJson = null;
        }

        Long rowId = null;
        if (data != null) {
            Object idValue = data.get("id");
            if (idValue instanceof Number num) {
                rowId = num.longValue();
            } else if (idValue != null) {
                try {
                    rowId = Long.parseLong(idValue.toString());
                } catch (NumberFormatException ignored) {
                    rowId = null;
                }
            }
        }

        Instant eventTs = event.tsMs() != null ? Instant.ofEpochMilli(event.tsMs()) : null;
        AuditLogEvent.Source source = event.source();

        return new AuditLogRow(
                source != null ? source.db() : null,
                source != null ? source.schema() : null,
                source != null ? source.table() : null,
                rowId,
                event.op(),
                dataJson,
                source != null ? source.lsn() : null,
                source != null ? source.txId() : null,
                eventTs
        );
    }

    public long estimateBytes(String json) {
        return json != null ? json.getBytes().length : 0L;
    }

    public record AuditLogRow(
            String dbName,
            String schemaName,
            String tableName,
            Long rowId,
            String op,
            String dataJson,
            Long lsn,
            Long txId,
            Instant eventTs
    ) {}
}
