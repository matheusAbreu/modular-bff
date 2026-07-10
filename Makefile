DB_NAME=postgres
DB_DSN=host=localhost port=5432 user=postgres password=postgres dbname=$(DB_NAME) sslmode=disable
DOCKER_COMPOSE=docker compose --env-file .env --file ./config/docker/compose.yaml
_goose_ = goose -dir ./config/database/migrations postgres "$(DB_DSN)"
__MIGRATION_NAME__ =
DOCKER_TAG=bff
DOCKER_APP_VERSION=latest

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

debug/build: deps/kotlin ## Build the application locally, for development and debugging
ifeq ($(OS),Windows_NT)
	gradlew.bat clean quarkusBuild -xtest
else
	./gradlew clean quarkusBuild -xtest
endif

container/build: ## Build the production image with both API modes active
	docker build \
	--build-arg BFF_API_EXTERNAL_ACTIVE=true \
	--build-arg BFF_API_INTERNAL_ACTIVE=true \
	-t $(DOCKER_TAG):$(DOCKER_APP_VERSION) \
	--no-cache -f ./config/docker/Dockerfile .

container/build/default: ## Build the production image with neither internal nor external routes
	docker build \
	--build-arg BFF_API_EXTERNAL_ACTIVE=false \
	--build-arg BFF_API_INTERNAL_ACTIVE=false \
	-t $(DOCKER_TAG)-default:$(DOCKER_APP_VERSION) \
	--no-cache -f ./config/docker/Dockerfile .

container/build/external: ## Build the production image with only the external routes
	docker build \
	--build-arg BFF_API_EXTERNAL_ACTIVE=true \
	--build-arg BFF_API_INTERNAL_ACTIVE=false \
	-t $(DOCKER_TAG)-external:$(DOCKER_APP_VERSION) \
	--no-cache -f ./config/docker/Dockerfile .

container/build/internal: ## Build the production image with only the internal routes
	docker build \
	--build-arg BFF_API_EXTERNAL_ACTIVE=false \
	--build-arg BFF_API_INTERNAL_ACTIVE=true \
	-t $(DOCKER_TAG)-internal:$(DOCKER_APP_VERSION) \
	--no-cache -f ./config/docker/Dockerfile .

container/build/variants: container/build/default container/build/internal container/build/external ## Build the three production variants analysed by the TCC

define inspect_variant
	@echo ""
	@echo "═══ $(1):$(DOCKER_APP_VERSION) ═══"
	@echo "--- classes compiladas em app/*.jar ---"
	@docker run --rm --entrypoint sh $(1):$(DOCKER_APP_VERSION) -c \
		'jar tf /deployments/app/*.jar | grep "adapters/api/v1/.*\.class$$" | sort' \
		|| echo "    (nenhuma)"
	@echo "--- pontos de entrada em quarkus/generated-bytecode.jar ---"
	@docker run --rm --entrypoint sh $(1):$(DOCKER_APP_VERSION) -c \
		'jar tf /deployments/quarkus/generated-bytecode.jar | grep -E "adapters/api/v1/.*(_Bean|quarkusrestinvoker)" | sort' \
		|| echo "    (nenhum)"
endef

artifact/entrypoints/default: ## Inspect the default production artifact (no internal/external routes)
	$(call inspect_variant,$(DOCKER_TAG)-default)

artifact/entrypoints/internal: ## Inspect the internal production artifact
	$(call inspect_variant,$(DOCKER_TAG)-internal)

artifact/entrypoints/external: ## Inspect the external production artifact
	$(call inspect_variant,$(DOCKER_TAG)-external)

artifact/analysis: artifact/entrypoints/default artifact/entrypoints/internal artifact/entrypoints/external ## Inspect the three production variants side by side

container/run:  ## Run the application in a Docker container in default mode
	docker run  --name=$(DOCKER_TAG) \
	-e API_PORT=9000 \
	-e POSTGRES_URL="jdbc:postgresql://postgres-db:5432/postgres" \
	--network=modular-bff_default \
	-p 9000:9000 $(DOCKER_TAG):$(DOCKER_APP_VERSION)

container/run/external:  ## Run the application in a Docker container in external mode
	docker run  --name=$(DOCKER_TAG)-external \
	-e API_PORT=9000 \
	-e POSTGRES_URL="jdbc:postgresql://postgres-db:5432/postgres" \
	--network=modular-bff_default \
	-p 9001:9000 $(DOCKER_TAG)-external:$(DOCKER_APP_VERSION)

container/run/internal:  ## Run the application in a Docker container in internal mode
	docker run  --name=$(DOCKER_TAG)-internal \
	-e API_PORT=9000 \
	-e POSTGRES_URL="jdbc:postgresql://postgres-db:5432/postgres" \
	--network=modular-bff_default \
	-p 9002:9000 $(DOCKER_TAG)-internal:$(DOCKER_APP_VERSION)

app/build/run/default: container/build container/run ## Build and run the application in a Docker container in default mode

app/build/run/external: container/build/external container/run/external ## Build and run the application in a Docker container in external mode

app/build/run/internal: container/build/internal container/run/internal ## Build and run the application in a Docker container in internal mode

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

ci/test: deps/up ## Run all tests in CI environment 
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