package org.acme.adapters.api.liveness

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness

@Liveness
@ApplicationScoped
class ApplicationLiveness : HealthCheck {
    override fun call() = HealthCheckResponse.up("liveness check")
}