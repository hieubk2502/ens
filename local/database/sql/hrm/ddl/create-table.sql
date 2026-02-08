CREATE TABLE IF NOT EXISTS hrm.test (
    id int8,
    name varchar(255)
);

CREATE TABLE IF NOT EXISTS hrm.hrm_organization (
    id BIGSERIAL PRIMARY KEY,

    parent_id BIGINT,
    prev_id BIGINT,

    code VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,

    CONSTRAINT uk_org_code UNIQUE (code),

    CONSTRAINT fk_org_parent
        FOREIGN KEY (parent_id)
        REFERENCES hrm.hrm_organization (id)
        ON DELETE SET NULL,

    CONSTRAINT fk_org_prev
        FOREIGN KEY (prev_id)
        REFERENCES hrm.hrm_organization (id)
        ON DELETE SET NULL
);

CREATE INDEX idx_hrm_organization_parent_id ON hrm.hrm_organization(parent_id);
CREATE INDEX idx_hrm_organization_prev_id   ON hrm.hrm_organization(prev_id);

CREATE TABLE IF NOT EXISTS audit.audit_log (
                           id BIGSERIAL PRIMARY KEY,
                           schema_name varchar(100) not null,
                           table_name varchar(100) not null,
                           row_id BIGINT NOT NULL,

                           op CHAR(1) NOT NULL, -- c/u/d

                           after_data JSONB,

                           lsn BIGINT NOT NULL,
                           tx_id BIGINT,

                           event_ts TIMESTAMPTZ,

                           created_at TIMESTAMPTZ DEFAULT now(),

                           UNIQUE (schema_name, table_name, lsn)
);
