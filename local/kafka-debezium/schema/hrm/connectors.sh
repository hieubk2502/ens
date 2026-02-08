#!/usr/bin/env bash
curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  -d '
      {
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

          "publication.autocreate.mode": "filtered",

          "table.include.list": "hrm.hrm_organization",

          "snapshot.mode": "initial",

          "transforms": "select",
          "transforms.select.type": "org.apache.kafka.connect.transforms.ReplaceField$Value",
          "transforms.select.whitelist": "before,after,op,source,ts_ms"
        }
      }
'
