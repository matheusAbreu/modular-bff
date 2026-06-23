FROM gradle:9.2-jdk21-ubi-minimal AS builder

USER root

WORKDIR /build-dir

RUN mkdir -p /tmp/cache

COPY . .

RUN gradle dependencies -g /tmp/cache --no-daemon && \
    gradle assemble -g /tmp/cache --no-daemon

FROM amazoncorretto:21-alpine-jdk

WORKDIR /deployments

COPY --from=builder /build-dir/init.sh /deployments
COPY --from=builder /build-dir/build/quarkus-app/lib/ /deployments/lib/
COPY --from=builder /build-dir/build/quarkus-app/*.jar /deployments/
COPY --from=builder /build-dir/build/quarkus-app/app/ /deployments/app/
COPY --from=builder /build-dir/build/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 9000

RUN addgroup -S bff-group && \
    adduser -S bff -G bff-group && \
    chown bff:bff-group /deployments/

USER bff

ENTRYPOINT ["sh", "init.sh"]