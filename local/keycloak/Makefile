.PHONY: up down restart logs


DC=docker compose

KEYCLOAK_COMPOSE=./docker-compose.yml

up:
	$(DC) -f $(KEYCLOAK_COMPOSE) up -d
down:
	$(DC) -f $(KEYCLOAK_COMPOSE) down
logs:
	$(DC) -f $(KEYCLOAK_COMPOSE) logs -f
logs-tail:
	$(DC) -f $(KEYCLOAK_COMPOSE) logs --tail=100

