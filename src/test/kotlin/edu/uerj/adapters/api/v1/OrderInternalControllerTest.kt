package edu.uerj.adapters.api.v1

import edu.uerj.builder.DeliveryBuilder
import edu.uerj.builder.OrderBuilder
import edu.uerj.config.ProviderToTests
import edu.uerj.domain.enums.OrderStatus
import edu.uerj.services.OrderService
import io.mockk.every
import io.mockk.mockk
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@QuarkusTest
@TestProfile(ProviderToTests::class)
class OrderInternalControllerTest {
    @InjectMock
    private var orderService: OrderService = mockk(relaxed = true)

    @ParameterizedTest
    @EnumSource(OrderStatus::class)
    fun `should return 202 and updated order when status update is successful`(status: OrderStatus) {
        val deliveryId = (1..100).random().toLong()
        val orderId = (0..100).random().toLong()
        val delivery =
            DeliveryBuilder(
                id = deliveryId,
                status = status.description,
            ).build()
        val order =
            OrderBuilder(
                id = orderId,
                status = status.description,
                deliverys = mutableListOf(delivery),
            ).build()

        every { orderService.updateStatus(orderId, deliveryId, status) } returns order

        RestAssured
            .given()
            .pathParam("orderId", orderId)
            .pathParam("deliveryId", deliveryId)
            .pathParam("newStatus", status)
            .`when`()
            .patch("/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}")
            .then()
            .statusCode(202)
            .body("id", `is`(orderId.toInt()))
            .body("status", `is`(status.description))
            .body("deliverys[0].id", `is`(delivery.id.toInt()))
            .body("deliverys[0].status", `is`(status.description))
    }

    @Test
    fun `should return 404 when order or delivery not found during update`() {
        every { orderService.updateStatus(any(), any(), any()) } returns null

        RestAssured
            .given()
            .pathParam("orderId", 999L)
            .pathParam("deliveryId", 888L)
            .pathParam("newStatus", OrderStatus.PAID.name)
            .`when`()
            .patch("/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}")
            .then()
            .statusCode(404)
    }

    @Test
    fun `should return 400 when service throws IllegalArgumentException`() {
        val errorMessage = "Invalid status transition"
        every { orderService.updateStatus(any(), any(), any()) } throws IllegalArgumentException(errorMessage)

        RestAssured
            .given()
            .pathParam("orderId", 1L)
            .pathParam("deliveryId", 10L)
            .pathParam("newStatus", OrderStatus.NEW_ORDER.name)
            .`when`()
            .patch("/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}")
            .then()
            .statusCode(400)
            .body("message", `is`(errorMessage))
    }

    @Test
    fun `should return 500 when updateStatus throws unexpected exception`() {
        every { orderService.updateStatus(any(), any(), any()) } throws RuntimeException("Unexpected error")

        RestAssured
            .given()
            .pathParam("orderId", 1L)
            .pathParam("deliveryId", 10L)
            .pathParam("newStatus", OrderStatus.PAID.name)
            .`when`()
            .patch("/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}")
            .then()
            .statusCode(500)
            .body("message", `is`("Unexpected error"))
    }
}
