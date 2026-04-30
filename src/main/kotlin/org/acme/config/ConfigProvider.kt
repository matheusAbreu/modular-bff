package org.acme.config

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class ConfigProvider {

    @ConfigProperty(name = "modular-bff.api.web.active")
    lateinit var apiWebActive: String

    @ConfigProperty(name = "modular-bff.api.app.active")
    lateinit var apiAppActive: String

    
    fun isApiWebActive() = apiWebActive.toBoolean()

    fun isApiAppActive() = apiAppActive.toBoolean()

    
}