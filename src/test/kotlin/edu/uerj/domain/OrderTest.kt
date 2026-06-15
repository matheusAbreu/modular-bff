package edu.uerj.domain

import edu.uerj.builder.CustomerBuilder
import edu.uerj.builder.DeliveryBuilder
import edu.uerj.builder.OrderBuilder
import edu.uerj.domain.enums.OrderStatus
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@QuarkusTest
class OrderTest {
    @Test
    fun `should create order using parameterized constructor`() {
        val customer = CustomerBuilder().build()
        val status = OrderStatus.NEW_ORDER.description
        val total = BigDecimal.valueOf(100.0)
        val discount = BigDecimal.valueOf(10.0)
        val freight = BigDecimal.valueOf(15.0)

        val order = Order(status, total, discount, freight, customer)

        assertNotNull(order)
        assertEquals(status, order.status)
        assertEquals(total, order.total)
        assertEquals(discount, order.discount)
        assertEquals(freight, order.freight)
        assertEquals(customer, order.customer)
    }

    @Test
    fun `checkOrderStatus should update status to the lowest delivery status when lower than DELIVERED`() {
        val order = OrderBuilder(status = OrderStatus.DELIVERED.description).build()

        val delivery1 = DeliveryBuilder(status = OrderStatus.PAID.description).build()
        val delivery2 = DeliveryBuilder(status = OrderStatus.WAITING_PAYMENT.description).build()

        order.deliverys.addAll(listOf(delivery1, delivery2))

        order.checkOrderStatus()

        assertEquals(OrderStatus.WAITING_PAYMENT.description, order.status)
    }

    @Test
    fun `checkOrderStatus should not update status when lowest delivery status is DELIVERED`() {
        val initialStatus = OrderStatus.NEW_ORDER.description
        val order = OrderBuilder(status = initialStatus).build()

        val delivery = DeliveryBuilder(status = OrderStatus.DELIVERED.description).build()
        order.deliverys.add(delivery)

        order.checkOrderStatus()

        assertEquals(initialStatus, order.status)
    }

    @Test
    fun `checkOrderStatus should not change status if there are no deliveries`() {
        val initialStatus = OrderStatus.NEW_ORDER.description
        val order = OrderBuilder(status = initialStatus).build()

        order.checkOrderStatus()

        assertEquals(initialStatus, order.status)
    }

    @Test
    fun `checkOrderStatus should handle multiple deliveries with same status`() {
        val order = OrderBuilder(status = OrderStatus.DELIVERED.description).build()

        val delivery1 = DeliveryBuilder(status = OrderStatus.PAID.description).build()
        val delivery2 = DeliveryBuilder(status = OrderStatus.PAID.description).build()

        order.deliverys.addAll(listOf(delivery1, delivery2))

        order.checkOrderStatus()

        assertEquals(OrderStatus.PAID.description, order.status)
    }
}
