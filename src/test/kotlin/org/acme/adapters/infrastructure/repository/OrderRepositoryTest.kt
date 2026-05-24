package org.acme.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.acme.domain.Order
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@QuarkusTest
class OrderRepositoryTest {

    @Inject
    lateinit var orderRepository: OrderRepository

    @Test
    fun `should save and find an order`() {
        val order = Order().apply {
            id = 1L
            status = "PENDING"
            total = 100.0
            createdAt = LocalDateTime.now()
        }

        val savedOrder = orderRepository.save(order)
        assertNotNull(savedOrder)
        assertEquals(1L, savedOrder.id)

        val foundOrder = orderRepository.findById(1L)
        assertNotNull(foundOrder)
        assertEquals("PENDING", foundOrder?.status)
        assertEquals(100.0, foundOrder?.total)
    }

    @Test
    fun `should return null when order not found`() {
        val foundOrder = orderRepository.findById(999L)
        assertNull(foundOrder)
    }
}
