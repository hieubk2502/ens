CREATE USER debezium WITH PASSWORD 'debezium';
ALTER USER debezium REPLICATION;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO debezium;


-- Cho phép connect DB
GRANT CONNECT ON DATABASE ens TO debezium;

-- Rất quan trọng: cho phép tạo publication
GRANT CREATE ON DATABASE ens TO debezium;

-- Quyền replication (bắt buộc)
ALTER USER debezium REPLICATION;

-- Quyền đọc schema
GRANT USAGE ON SCHEMA hrm TO debezium;
GRANT SELECT ON ALL TABLES IN SCHEMA hrm TO debezium;

-- Cho các bảng tạo sau này
ALTER DEFAULT PRIVILEGES IN SCHEMA hrm
GRANT SELECT ON TABLES TO debezium;
