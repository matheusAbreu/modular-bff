package edu.uerj.config

import io.quarkus.test.junit.QuarkusTestProfile

class ConfigProviderInternalMode: QuarkusTestProfile {
   override fun getConfigOverrides(): Map<String, String> = mapOf("modular-bff.api.mode" to "internal")
}
