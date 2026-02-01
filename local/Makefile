DC=docker compose

# database
DB_COMPOSE=./database/docker-compose.yml
DB_SQL_PATH=./database
DB_CONTAINER=postgres
DB_USER=ens-user
DB_NAME=ens

#kafka & debezium
KAFKA_COMPOSE=./kafka-debezium/docker-compose.yml

# kafka & debezium
kafka-up:
	$(DC) -f $(KAFKA_COMPOSE) up -d
kafka-down:
	${DC} -f $(KAFKA_COMPOSE) down

.PHONY: \
	db-up \
	logs clean

db-up:
	$(DC) -f $(DB_COMPOSE) up -d
db-down:
	$(DC) -f $(DB_COMPOSE) down
db-logs:
	$(DC) -f $(DB_COMPOSE) logs -f
db-logs-tail:
	$(DC) -f $(DB_COMPOSE) logs --tail=100


# ens
db.up.schema:
	@for f in $(DB_SQL_PATH)/sql/create-schema.sql; do \
		echo "Running $$f"; \
		docker exec -i $(DB_CONTAINER) psql -U $(DB_USER) -d $(DB_NAME) < $$f; \
	done

## hrm
db.hrm.ddl:
	@for f in $(DB_SQL_PATH)/sql/hrm/ddl/*.sql; do \
		echo "Running $$f"; \
		docker exec -i $(DB_CONTAINER) psql -U $(DB_USER) -d $(DB_NAME) < $$f; \
	done

db.hrm.dml:
	@for f in $(DB_SQL_PATH)/sql/hrm/dml/*.sql; do \
		echo "Running $$f"; \
		docker exec -i $(DB_CONTAINER) psql -U $(DB_USER) -d $(DB_NAME) < $$f; \
	done

#
up:
	make db-up

down:
	make db-down

logs:
	make db-logs

logs.tail:
	make db-logs-tail

clean:
	$(DC) -f $(DB-COMPOSE) down -v



