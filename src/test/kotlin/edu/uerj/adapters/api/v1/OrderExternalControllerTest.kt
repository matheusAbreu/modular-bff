package edu.uerj.adapters.api.v1

import edu.uerj.builder.NewOrderDTOBuilder
import edu.uerj.builder.OrderBuilder
import edu.uerj.config.ProviderToTests
import edu.uerj.services.OrderService
import io.mockk.every
import io.mockk.mockk
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
@TestProfile(ProviderToTests::class)
class OrderExternalControllerTest {
    @InjectMock
    private var orderService: OrderService = mockk(relaxed = true)

    @Test
    fun `should return 201 and created order when creation is successful`() {
        val newOrder = NewOrderDTOBuilder().build()
        val order = OrderBuilder(id = 123L).build()

        every { orderService.createOrder(any()) } returns order

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(newOrder)
            .`when`()
            .post("/v1/order/create")
            .then()
            .statusCode(201)
            .body("id", `is`(123))
    }

    @Test
    fun `should return 400 when items are empty`() {
        val newOrder = NewOrderDTOBuilder()
            .withItems()
            .build()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(newOrder)
            .`when`()
            .post("/v1/order/create")
            .then()
            .statusCode(400)
            .body("message", `is`("The order contains no items to be processed."))
    }

    @Test
    fun `should return 400 when payment methods are empty`() {
        val newOrder = NewOrderDTOBuilder()
            .withPaymentMethods()
            .build()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(newOrder)
            .`when`()
            .post("/v1/order/create")
            .then()
            .statusCode(400)
            .body("message", `is`("The order has no payments to be processed."))
    }

    @Test
    fun `should return 400 when service throws IllegalArgumentException`() {
        val newOrder = NewOrderDTOBuilder().build()
        val errorMessage = "Invalid data"
        every { orderService.createOrder(any()) } throws IllegalArgumentException(errorMessage)

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(newOrder)
            .`when`()
            .post("/v1/order/create")
            .then()
            .statusCode(400)
            .body("message", `is`(errorMessage))
    }

    @Test
    fun `should return 500 when createOrder throws unexpected exception`() {
        val newOrder = NewOrderDTOBuilder().build()
        every { orderService.createOrder(any()) } throws RuntimeException("System error")

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(newOrder)
            .`when`()
            .post("/v1/order/create")
            .then()
            .statusCode(500)
            .body("message", `is`("System error"))
    }
}
