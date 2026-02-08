DC=docker compose

# database
DB_COMPOSE=./docker-compose.yml

audit-up:
	$(DC) -f $(DB_COMPOSE) up -d

audit-down:
	$(DC) -f $(DB_COMPOSE) down

build-jar:
	./mvnw clean install -DskipTests
