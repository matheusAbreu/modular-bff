DB_NAME=postgres
DB_DSN=host=localhost port=5432 user=postgres password=postgres dbname=$(DB_NAME) sslmode=disable
DOCKER_COMPOSE=docker compose --env-file .env --file ./config/docker/compose.yaml
_goose_ = goose -dir ./config/database/migrations postgres "$(DB_DSN)"
__MIGRATION_NAME__ = 

deps/install:
	go install github.com/pressly/goose/v3/cmd/goose@latest

db/create-migration:
	$(_goose_) create $(__MIGRATION_NAME__) sql

db/migrate-up:
	$(_goose_) up

db/migrate-down:
	$(_goose_) down

db/seed:
	@echo "Populando a base de dados..."
	$(DOCKER_COMPOSE) exec -T postgres-db psql -U postgres -d $(DB_NAME) < ./config/database/test-data/seed.sql

dev:
ifeq ($(OS),Windows_NT)
	gradlew.bat clean assemble quarkusDev
else
	./gradlew clean assemble quarkusDev
endif

docker/up:
	$(DOCKER_COMPOSE) up --build -d

docker/down:
	$(DOCKER_COMPOSE) down

db/wait:
	@echo "Aguardando $(DB_NAME) ficar pronto..."
	@until $(DOCKER_COMPOSE) exec postgres-db pg_isready -U postgres > /dev/null 2>&1; do \
		sleep 1; \
	done
	@echo "$(DB_NAME) está pronto!"

deps/up: docker/up db/wait db/migrate-up 

deps/up/populated: deps/up db/seed

deps/down: docker/down

deps/restart: deps/down deps/up

deps/restart/populated: deps/down deps/up/populated