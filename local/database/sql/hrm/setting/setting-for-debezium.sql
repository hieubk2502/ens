ALTER SYSTEM SET max_wal_size = '16GB';
ALTER SYSTEM SET min_wal_size = '4GB';

SELECT pg_reload_conf();

SHOW max_wal_size;
SHOW min_wal_size;
SHOW checkpoint_timeout;

--      theo dõi WAL
SELECT
    slot_name,
    active,
    pg_size_pretty(
            pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)
    ) AS retained_wal
FROM pg_replication_slots;
