package edu.uerj.services

import edu.uerj.adapters.infrastructure.repository.OrderRepository
import edu.uerj.domain.Order
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`

@QuarkusTest
class OrderServiceTest {
    @Inject
    lateinit var orderService: OrderService

    @InjectMock
    lateinit var orderRepository: OrderRepository

    @Test
    fun `should return order when it exists`() {
        val order = Order()
        order.id = 1L

        `when`(orderRepository.findById(1L)).thenReturn(order)

        val result = orderService.getOrder(1L)

        assertEquals(order, result)
        assertEquals(1L, result?.id)
    }

    @Test
    fun `should return null when order does not exist`() {
        `when`(orderRepository.findById(anyLong())).thenReturn(null)

        val result = orderService.getOrder(999L)

        assertNull(result)
    }
}
