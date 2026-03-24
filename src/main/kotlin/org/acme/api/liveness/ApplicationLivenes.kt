package org.acme.api.liveness

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness

@Liveness
@ApplicationScoped
class ApplicationLivenes : HealthCheck {
    override fun call() = HealthCheckResponse.up("liveness check")
}