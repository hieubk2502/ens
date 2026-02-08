DC=docker compose

#kafka & debezium
KAFKA_COMPOSE=./docker-compose.yml

# kafka & debezium
kafka-up:
	$(DC) -f $(KAFKA_COMPOSE) up -d
kafka-down:
	${DC} -f $(KAFKA_COMPOSE) down

.PHONY: \
	db-up \
	logs clean
