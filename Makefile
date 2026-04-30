DB_NAME=postgres
DB_DSN=host=localhost port=5432 user=postgres password=postgres dbname=$(DB_NAME) sslmode=disable
_goose_ = goose -dir ./config/migrations postgres "$(DB_DSN)"
__MIGRATION_NAME__ = 

deps/install:
	go install github.com/pressly/goose/v3/cmd/goose@latest

db/create-migration:
	$(_goose_) create $(__MIGRATION_NAME__) sql

db/migrate-up:
	$(_goose_) up

db/migrate-down:
	$(_goose_) down

dev:
ifeq ($(OS),Windows_NT)
	gradlew.bat clean assemble quarkusDev
else
	./gradlew clean assemble quarkusDev
endif

docker/up:
	docker compose --env-file .env --file ./config/docker/compose.yaml up --build -d

docker/down:
	docker compose --env-file .env --file ./config/docker/compose.yaml down

db/wait:
	@echo "Aguardando $(DB_NAME) ficar pronto..."
	@until docker compose --env-file .env --file ./config/docker/compose.yaml exec postgres-db pg_isready -U postgres > /dev/null 2>&1; do \
		sleep 1; \
	done
	@echo "$(DB_NAME) está pronto!"

deps/up: docker/up db/wait db/migrate-up

deps/down: docker/down

deps/restart: deps/down deps/up