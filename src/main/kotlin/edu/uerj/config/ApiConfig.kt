package edu.uerj.config

import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.Application
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.info.Info

@OpenAPIDefinition(info = Info(
    title = "Modular BFF API",
    version = "1.0.0",
    description = "API focused on supporting the frontend x project",
    ))
@ApplicationPath("/")
class ApiConfig: Application()