package edu.uerj.config

import io.quarkus.test.junit.QuarkusTestProfile

class ProviderToTests : QuarkusTestProfile {
    override fun getConfigOverrides(): Map<String, String> =
        mapOf(
            "modular-bff.api.mode.internal.enable" to "true",
            "modular-bff.api.mode.external.enable" to "true",
        )
}
