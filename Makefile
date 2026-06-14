DB_NAME=postgres
DB_DSN=host=localhost port=5432 user=postgres password=postgres dbname=$(DB_NAME) sslmode=disable
DOCKER_COMPOSE=docker compose --env-file .env --file ./config/docker/compose.yaml
_goose_ = goose -dir ./config/database/migrations postgres "$(DB_DSN)"
__MIGRATION_NAME__ =

# Load envs from .env file if it exists
-include .env
export

help:  ## Display this help
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make [target]\n\nTargets:\n"} /^[a-zA-Z_\/\-]+:.*##/ { printf "\033[36m%-20s\033[0m \t%s\n", $$1, $$2 }' $(MAKEFILE_LIST)


deps/kotlin: ## Clean and refresh Kotlin/Gradle dependencies
ifeq ($(OS),Windows_NT)
	gradlew.bat clean build --status --refresh-dependencies
else
	./gradlew clean build --status --refresh-dependencies
endif

deps/go: ## Install required Go dependencies (goose)
	go install github.com/pressly/goose/v3/cmd/goose@latest

deps/install: deps/kotlin deps/go ## Install all project dependencies (Kotlin and Go)

db/create-migration: ## Create a new database migration file (pass __MIGRATION_NAME__=name)
	$(_goose_) create $(__MIGRATION_NAME__) sql

db/migrate-up: ## Apply all pending database migrations
	$(_goose_) up

db/migrate-down: ## Roll back the last database migration
	$(_goose_) down

db/seed: ## Populate the database with initial/test data
	@echo "Populando a base de dados..."
	$(DOCKER_COMPOSE) exec -T postgres-db psql -U postgres -d $(DB_NAME) < ./config/database/test-data/seed.sql

clean: ## Clean the project
ifeq ($(OS),Windows_NT)
	gradlew.bat clean
else
	./gradlew clean
endif

dev: ## Start the application in Quarkus development mode
ifeq ($(OS),Windows_NT)
	gradlew.bat clean assemble quarkusDev
else
	./gradlew clean assemble quarkusDev
endif

docker/up: ## Start infrastructure containers in detached mode
	$(DOCKER_COMPOSE) up --build -d

docker/down: ## Stop and remove infrastructure containers
	$(DOCKER_COMPOSE) down

db/wait: ## Wait for the database container to be ready
	@echo "Aguardando $(DB_NAME) ficar pronto..."
	@until $(DOCKER_COMPOSE) exec postgres-db pg_isready -U postgres > /dev/null 2>&1; do \
		sleep 1; \
	done
	@echo "$(DB_NAME) está pronto!"


deps/up: docker/up db/wait db/migrate-up ## Start infrastructure, wait for DB, and run migrations

deps/up/populated: deps/up db/seed ## Start infrastructure with migrations and seeded data

deps/down: docker/down ## Shut down all infrastructure

deps/restart: deps/down deps/up ## Restart entire infrastructure (migrations applied)

deps/restart/populated: deps/down deps/up/populated ## Restart entire infrastructure with seeded data

test: deps/restart ## Run all tests (restarts infrastructure first)
ifeq ($(OS),Windows_NT)
	gradlew.bat clean test
else
	./gradlew clean test
endif

lint: ## Run all lint
ifeq ($(OS),Windows_NT)
	gradlew.bat ktlintCheck
else
	./gradlew ktlintCheck
endif

detekt: ## Run Detekt static code analysis
ifeq ($(OS),Windows_NT)
	gradlew.bat detekt 
else
	./gradlew detekt 
endif