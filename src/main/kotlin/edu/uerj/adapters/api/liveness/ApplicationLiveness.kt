package edu.uerj.adapters.api.liveness

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import jakarta.ws.rs.GET
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses

@Liveness
@ApplicationScoped
class ApplicationLiveness : HealthCheck {
    override fun call() = HealthCheckResponse.up("liveness check")
}