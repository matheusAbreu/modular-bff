package edu.uerj.config

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class ConfigProvider {
    @ConfigProperty(name = "modular-bff.api.mode")
    lateinit var apiWebActive: String

    fun isApiInternalModeActive() = apiWebActive.equals(API_MODE_INTERNAL, ignoreCase = true)

    fun isApiExternalModeActive() = apiWebActive.equals(API_MODE_EXTARNAL, ignoreCase = true)

    companion object {
        const val API_MODE_INTERNAL = "internal"
        const val API_MODE_EXTARNAL = "external"
    }
}
