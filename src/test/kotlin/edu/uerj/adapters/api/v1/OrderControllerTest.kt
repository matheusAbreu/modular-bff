package edu.uerj.adapters.api.v1

import edu.uerj.adapters.api.dto.common.PaginatedResult
import edu.uerj.domain.Delivery
import edu.uerj.domain.Order
import edu.uerj.domain.enums.OrderStatus
import edu.uerj.services.OrderService
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import java.math.BigDecimal
import java.time.LocalDateTime

@QuarkusTest
class OrderControllerTest {
    private lateinit var orderController: OrderController
    private lateinit var orderService: OrderService

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        orderService = mockk(relaxed = true)

        orderController = OrderController(orderService)
    }

    @Test
    fun `should return 200 and order when found`() {
        val order =
            Order().apply {
                id = 1L
                status = "PENDING"
                total = BigDecimal.valueOf(100.0)
                createdAt = LocalDateTime.now()
                updatedAt = LocalDateTime.now()
            }

        `when`(orderService.getOrder(1L)).thenReturn(order)

        RestAssured
            .given()
            .`when`()
            .get("/v1/order/1")
            .then()
            .statusCode(200)
            .body("id", `is`(1))
            .body("status", `is`("PENDING"))
            .body("total", `is`(100.0f))
    }

    @Test
    fun `should return 404 when order not found`() {
        `when`(orderService.getOrder(999L)).thenReturn(null)

        RestAssured
            .given()
            .`when`()
            .get("/v1/order/999")
            .then()
            .statusCode(404)
            .body("message", `is`("Order with id 999 not found"))
    }

    @Test
    fun `should return 500 when service throws exception`() {
        `when`(orderService.getOrder(1L)).thenThrow(RuntimeException("Database error"))

        RestAssured
            .given()
            .`when`()
            .get("/v1/order/1")
            .then()
            .statusCode(500)
            .body("message", `is`("Database error"))
    }

    @Test
    fun `should return 200 and list of orders`() {
        val order =
            Order().apply {
                id = 1L
                status = "COMPLETED"
                total = BigDecimal.TEN
            }
        val pagedResult =
            PaginatedResult(
                items = mutableListOf(order),
                limit = 10L,
                total = 1L,
            )

        `when`(orderService.listAll(10L, 0L)).thenReturn(pagedResult)

        RestAssured
            .given()
            .queryParam("limitPerPage", 10)
            .queryParam("page", 0)
            .`when`()
            .get("/v1/order/list")
            .then()
            .statusCode(200)
            .body("items", hasSize<Any>(1))
            .body("items[0].id", `is`(1))
            .body("items[0].status", `is`("COMPLETED"))
            .body("total", `is`(1))
    }

    @Test
    fun `should return 400 when service returns null items`() {
        val pagedResult =
            PaginatedResult<Order>(
                items = null,
                limit = 10L,
                total = 0L,
            )

        `when`(orderService.listAll(10L, 0L)).thenReturn(pagedResult)

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
        `when`(orderService.listAll(10L, 0L)).thenThrow(RuntimeException("List error"))

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

    @Test
    fun `should return 202 and updated order when status update is successful`() {
        val order =
            Order().apply {
                id = 1L
                status = OrderStatus.PAID.description
            }
        val delivery =
            Delivery().apply {
                id = 10L
                status = OrderStatus.PAID.description
            }
        order.deliverys.add(delivery)

        `when`(orderService.updateStatus(1L, 10L, OrderStatus.PAID)).thenReturn(order)

        RestAssured
            .given()
            .pathParam("orderId", 1L)
            .pathParam("deliveryId", 10L)
            .pathParam("newStatus", OrderStatus.PAID.name)
            .`when`()
            .patch("/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}")
            .then()
            .statusCode(202)
            .body("id", `is`(1))
            .body("status", `is`(OrderStatus.PAID.description))
    }

    @Test
    fun `should return 404 when order or delivery not found during update`() {
        `when`(orderService.updateStatus(anyLong(), anyLong(), any(OrderStatus::class.java))).thenReturn(null)

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
        `when`(orderService.updateStatus(anyLong(), anyLong(), any(OrderStatus::class.java)))
            .thenThrow(IllegalArgumentException(errorMessage))

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
        `when`(orderService.updateStatus(anyLong(), anyLong(), any(OrderStatus::class.java)))
            .thenThrow(RuntimeException("Unexpected error"))

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
