package org.acme.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.domain.Customer
import org.acme.domain.Order
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@QuarkusTest
@Transactional
class OrderRepositoryTest {

    @Inject
    lateinit var orderRepository: OrderRepository

    @Test
    fun `should save and find an order`() {
        val testId = (1..10000).random()
        val client = Customer(
            name = "testUser${testId}",
            email = "test${testId}@work.com",
            document = "123.456.789-${testId}",
            documentType = "cpf",
            birthDate = LocalDateTime.now()
        )
        val order =  orderRepository.save(Order(
            status = "PENDING",
            total = 100.0,
            discount = 0.0,
            freight = 0.0,
            customer = client
        ))

        assertNotEquals(order.id, 0)
        assertNotNull(order.createdAt)
        assertNotNull(order.updatedAt)


        val foundOrder = orderRepository.findById(order.id!!)!!
        assertNotNull(foundOrder)
        assertEquals("PENDING", foundOrder.status)
        assertEquals(100.0, foundOrder.total)
    }

    @Test
    fun `should return null when order not found`() {
        val foundOrder = orderRepository.findById(Long.MAX_VALUE)
        assertNull(foundOrder)
    }
}
