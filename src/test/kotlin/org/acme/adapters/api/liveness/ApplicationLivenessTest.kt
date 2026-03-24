package org.acme.adapters.api.liveness

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
class ApplicationLivenessTest {

    @Test
    fun `it should check if liveness is ok`() {
        RestAssured.given()
            .`when`()
        .get("/health")
        .then()
        .statusCode(200)
        .body("status", equalTo("UP"))

    }

}