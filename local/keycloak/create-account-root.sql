DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_roles WHERE rolname = 'admin_keycloak'
    ) THEN
        CREATE ROLE admin_keycloak LOGIN PASSWORD 'admin';
    END IF;
END
$$;

GRANT CONNECT ON DATABASE iam TO admin_keycloak;
GRANT USAGE ON SCHEMA public TO admin_keycloak;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO admin_keycloak;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO admin_keycloak;
