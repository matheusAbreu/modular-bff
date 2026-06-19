package edu.uerj.config

import io.quarkus.test.junit.QuarkusTestProfile

class ConfigProviderExternalMode: QuarkusTestProfile {
   override fun getConfigOverrides(): Map<String, String> = mapOf("modular-bff.api.mode" to "external")
}
