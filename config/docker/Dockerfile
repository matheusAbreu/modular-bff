FROM gradle:9.2-jdk25-ubi-minimal AS builder

USER root

# App setup
ENV APP_DIR=/app
WORKDIR $APP_DIR

RUN mkdir -p /tmp/cache

COPY build.gradle.kts settings.gradle.kts ./

# Warm-up Gradle cache
RUN gradle dependencies -g /tmp/cache --no-daemon

COPY . .

# Build
RUN gradle assemble -g /tmp/cache --no-daemon

FROM amazoncorretto:25-alpine-jdk

RUN apk add --no-cache libc6-compat
RUN apk add --no-cache gcompat=1.1.0-r4
RUN apk update && apk add --no-cache tzdata

WORKDIR /deployments

COPY --from=builder /app/init.sh /deployments
COPY --from=builder /app/build/quarkus-app/lib/ /deployments/lib/
COPY --from=builder /app/build/quarkus-app/*.jar /deployments/
COPY --from=builder /app/build/quarkus-app/app/ /deployments/app/
COPY --from=builder /app/build/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080

RUN addgroup -S rabbit-bff-group && adduser -S rabbit-bff -G rabbit-bff-group && chown rabbit-bff:rabbit-bff-group /deployments/
USER rabbit-bff

ENTRYPOINT ["sh", "init.sh"]