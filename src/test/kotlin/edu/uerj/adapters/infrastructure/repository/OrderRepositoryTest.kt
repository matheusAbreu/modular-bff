package edu.uerj.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import edu.uerj.domain.Customer
import edu.uerj.domain.Order
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import java.math.BigDecimal
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
            total = BigDecimal.valueOf(100.0),
            discount = BigDecimal.valueOf(0.0),
            freight = BigDecimal.valueOf(0.0),
            customer = client
        ))

        assertNotEquals(order.id, 0)
        assertNotNull(order.createdAt)
        assertNotNull(order.updatedAt)


        val foundOrder = orderRepository.findById(order.id!!)!!
        assertNotNull(foundOrder)
        assertEquals("PENDING", foundOrder.status)
        assertEquals(0, BigDecimal.valueOf(100.0).compareTo(foundOrder.total))
    }

    @Test
    fun `should return null when order not found`() {
        val foundOrder = orderRepository.findById(Long.MAX_VALUE)
        assertNull(foundOrder)
    }
}
