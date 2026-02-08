# Notes for setting up Postgres for CDC

## Architecture overview (CDC taps into WAL)
```
          +------------------+         +---------------------------+
          |  App/Service     |  SQL    |  Postgres (Primary)       |
          +------------------+-------> |  - Shared Buffers         |
                                       |  - WAL Writer             |
                                       |  - WAL Files (pg_wal)     |
                                       +--------------+------------+
                                                      |
                                                      | logical decoding
                                                      v
                                       +--------------+------------+
                                       |  Replication Slot         |
                                       |  (logical, per CDC)       |
                                       +--------------+------------+
                                                      |
                                                      | walsender
                                                      v
          +------------------+         +---------------------------+
          |  CDC Connector   | <------ |  WAL Stream (logical)     |
          |  (Debezium/...)  |         +---------------------------+
          +------------------+
                  |
                  v
          +------------------+
          |  Sink/Queue/DWH  |
          +------------------+
```

## Write path (how data moves when a service saves)
Data flow when a service writes to Postgres and CDC consumes it:
```
App/Service
  |
  | 1) INSERT/UPDATE/DELETE
  v
Postgres
  |
  | 2) Changes are written to WAL (Write-Ahead Log)
  v
pg_wal
  |
  | 3) Logical decoding reads WAL via replication slot
  v
WAL Stream (logical)
  |
  | 4) CDC connector receives row-level change events
  v
Sink/Queue/DWH
```
- Step 1: service sends DML to Postgres.
- Step 2: Postgres writes WAL first, then applies changes to data files.
- Step 3: logical decoding reads WAL from the slot and converts it to table/row changes.
- Step 4: CDC pushes events to downstream systems (queue, lake, DWH...).

## Required settings
- Set `wal_level = logical`.
- Increase `max_wal_senders` and `max_replication_slots` based on CDC connections.
- Ensure `max_worker_processes` and `max_logical_replication_workers` are large enough.
- If using `pg_hba.conf`, allow CDC user/host.
  - Example: `host replication cdc_user 10.0.0.0/16 md5`
   ` ALTER SYSTEM SET max_wal_size = '16GB';
    ALTER SYSTEM SET min_wal_size = '4GB';
    SELECT pg_reload_conf();`


## Suggested Postgres parameters (reference)
- `wal_level = logical`
- `max_wal_senders = 10` (>= CDC + other replication)
- `max_replication_slots = 10` (>= number of logical slots)
- `max_logical_replication_workers = 4` (>= active subscriptions/slots)
- `max_worker_processes = 8` (>= logical workers + others)
- `wal_keep_size = 512MB` (depends on traffic)
- `max_slot_wal_keep_size = 4GB` (to avoid disk exhaustion)
- Balance `shared_buffers`, `work_mem`, `maintenance_work_mem` with workload

## Users and permissions
- Create a dedicated CDC user; do not use superuser.
- Minimum privileges:
  - `REPLICATION`
  - `SELECT` on required schemas/tables
- Ensure `GRANT` for new schemas if CDC must read them.

Example SQL:
```
CREATE ROLE cdc_user LOGIN REPLICATION PASSWORD '***';
GRANT CONNECT ON DATABASE appdb TO cdc_user;
GRANT USAGE ON SCHEMA public TO cdc_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO cdc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO cdc_user;
```

## Source setup
- Create a publication for required tables (or schemas).
- Create a logical replication slot for CDC.
- Align timezone/encoding with downstream systems (recommended `UTC`, `UTF8`).

Example SQL:
```
CREATE PUBLICATION cdc_pub FOR TABLE public.orders, public.users;
SELECT * FROM pg_create_logical_replication_slot('cdc_slot', 'pgoutput');
```

## pg_hba.conf
- Allow connections from CDC host and user.
- Use `hostssl` and certs if TLS is required.

Example:
```
host    replication  cdc_user  10.0.0.0/16  md5
host    appdb        cdc_user  10.0.0.0/16  md5
```

## WAL behavior and disk usage
- Control WAL growth with `wal_keep_size` and `max_slot_wal_keep_size`.
- Monitor disk usage; logical slots retain WAL if CDC is down.
- Consider `archive_mode`/`archive_command` for replay needs.
- Warning: logical slots do not expire automatically; WAL will be retained while inactive.
- Use `pg_replication_slots` to monitor LSN and slot status.

## Connections and performance
- Use a read-only connection path for CDC to avoid impacting primary traffic.
- Manage `max_connections` and pooling if CDC opens many sessions.
- Set `statement_timeout`/`idle_in_transaction_session_timeout` to avoid stuck sessions.
- Consider `wal_compression = on` if WAL volume is large.

## Security and operations
- Never use superuser for CDC.
- Log CDC connections (log_connection/log_disconnection) for audit.
- Have a process to drop unused slots to release WAL.
- Check `pg_stat_replication` to see replication lag.

## Quick checks
```
SHOW wal_level;
SELECT slot_name, active, restart_lsn FROM pg_replication_slots;
SELECT pid, state, sent_lsn, replay_lsn FROM pg_stat_replication;
```
