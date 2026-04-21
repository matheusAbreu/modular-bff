DB_DSN=host=localhost port=5432 user=postgres password=postgres dbname=postgres sslmode=disable
_goose_ = goose -dir ./config/migrations postgres "$(DB_DSN)"
__MIGRATION_NAME__ = "criacao_tabelas_status_produto"

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
	gradlew.bat quarkusDev
else
	./gradlew quarkusDev
endif

docker/up:
	docker compose --env-file .env --file ./config/docker/compose.yaml up --build -d

docker/down:
	docker compose --env-file .env --file ./config/docker/compose.yaml down