#!/usr/bin/env bash
curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  -d '{
        "name": "debezium-ens-hrm",
        "config": {
          "connector.class": "io.debezium.connector.postgresql.PostgresConnector",

          "database.hostname": "host.docker.internal",
          "database.port": "5432",
          "database.user": "ens-user",
          "database.password": "Aa123456@",
          "database.dbname": "ens",

          "topic.prefix": "ens",

          "plugin.name": "pgoutput",
          "slot.name": "ens_hrm_slot",

          "publication.autocreate.mode": "all_tables",

          "schema.include.list": "hrm.*",

          "snapshot.mode": "initial"
        }
      }
'