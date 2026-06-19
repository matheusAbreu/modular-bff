package edu.uerj.adapters.api.v1

import edu.uerj.adapters.api.dto.common.PaginatedResult
import edu.uerj.builder.OrderBuilder
import edu.uerj.domain.Order
import edu.uerj.services.OrderService
import io.mockk.every
import io.mockk.mockk
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

@QuarkusTest
class OrderGeneralControllerTest {
    @InjectMock
    private var orderService: OrderService = mockk(relaxed = true)

    @Test
    fun `should return 200 and order when found`() {
        val order = OrderBuilder().build()

        every { orderService.getOrder(order.id!!) } returns order

        RestAssured
            .given()
            .`when`()
            .get("/v1/order/${order.id}")
            .then()
            .statusCode(200)
            .body("id", `is`(order.id!!.toInt()))
            .body("status", `is`(order.status))
    }

    @Test
    fun `should return 404 when order not found`() {
        val randomValue = (0..10000).random()
        every { orderService.getOrder(any()) } returns null

        RestAssured
            .given()
            .`when`()
            .get("/v1/order/$randomValue")
            .then()
            .statusCode(404)
            .body("message", `is`("Order with id $randomValue not found"))
    }

    @Test
    fun `should return 500 when service throws exception`() {
        val randomValue = (0..10000).random()
        every { orderService.getOrder(any()) } throws RuntimeException("Database error")

        RestAssured
            .given()
            .`when`()
            .get("/v1/order/$randomValue")
            .then()
            .statusCode(500)
            .body("message", `is`("Database error"))
    }

    @Test
    fun `should return 200 and list of orders`() {
        val limitValue = (0..100).random()
        val order = OrderBuilder().build()
        val pagedResult =
            PaginatedResult(
                items = mutableListOf(order),
                limit = limitValue.toLong(),
                total = 100L,
            )

        every { orderService.listAll(any(), any()) } returns pagedResult

        RestAssured
            .given()
            .queryParam("limitPerPage", limitValue)
            .queryParam("page", 0)
            .`when`()
            .get("/v1/order/list")
            .then()
            .statusCode(200)
            .body("items", hasSize<Any>(1))
            .body("limit", `is`(limitValue))
            .body("quantityInPage", `is`(1))
    }

    @Test
    fun `should return 400 when service returns null items`() {
        val pagedResult =
            PaginatedResult<Order>(
                items = null,
                limit = 10L,
                total = 0L,
            )

        every { orderService.listAll(any(), any()) } returns pagedResult

        RestAssured
            .given()
            .queryParam("limitPerPage", 10)
            .queryParam("page", 0)
            .`when`()
            .get("/v1/order/list")
            .then()
            .statusCode(400)
    }

    @Test
    fun `should return 500 when listAll throws exception`() {
        every { orderService.listAll(any(), any()) } throws RuntimeException("List error")

        RestAssured
            .given()
            .queryParam("limitPerPage", 10)
            .queryParam("page", 0)
            .`when`()
            .get("/v1/order/list")
            .then()
            .statusCode(500)
            .body("message", `is`("List error"))
    }
}
