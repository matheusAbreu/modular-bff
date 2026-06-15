package edu.uerj.adapters.infrastructure.repository

import edu.uerj.domain.Order
import edu.uerj.domain.OrderItem
import edu.uerj.domain.Product
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@QuarkusTest
@Transactional
class OrderItemRepositoryTest {
    @Inject
    lateinit var orderItemRepository: OrderItemRepository

    @Inject
    lateinit var orderRepository: OrderRepository

    @Inject
    lateinit var productRepository: ProductRepository

    @Test
    fun `should save and find an order item`() {
        val testId = (1..10000).random()

        val product =
            Product().apply {
                this.sku = "OI-SKU-$testId"
                this.name = "Test Product for Item"
                this.description = "Test Description"
                this.price = BigDecimal.valueOf(10.0)
            }
        val savedProduct = productRepository.save(product)

        val order =
            Order().apply {
                this.status = "PENDING"
                this.total = BigDecimal.valueOf(10.0)
            }
        val savedOrder = orderRepository.save(order)

        val item =
            OrderItem(
                product = savedProduct,
                order = savedOrder,
                delivery = null,
                quantity = 1,
                unitPrice = BigDecimal.valueOf(10.0),
                totalPrice = BigDecimal.valueOf(10.0),
            )

        val savedItem = orderItemRepository.save(item)

        assertNotNull(savedItem.id)
        assertNotEquals(0L, savedItem.id)

        val foundItem = orderItemRepository.findById(savedItem.id)
        assertNotNull(foundItem)
        assertEquals(savedItem.quantity, foundItem?.quantity)
        assertEquals(savedItem.totalPrice, foundItem?.totalPrice)
        assertEquals(savedOrder.id, foundItem?.order?.id)
    }

    @Test
    fun `should return null when item not found`() {
        val foundItem = orderItemRepository.findById(-1L)
        assertNull(foundItem)
    }
}
