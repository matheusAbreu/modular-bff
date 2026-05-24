package org.acme.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.domain.OrderAddress
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@QuarkusTest
@Transactional
class OrderAddressRepositoryTest {

    @Inject
    lateinit var orderAddressRepository: OrderAddressRepository

    @Test
    fun `should save and find an order address`() {
        val testId = (1..10000).random()
        val address = OrderAddress(
            customer = null,
            order = null,
            zipCode = "87654-321",
            street = "Order Street $testId",
            number = "456",
            complement = null,
            city = "Order City",
            state = "OS"
        )

        val savedAddress = orderAddressRepository.save(address)

        assertNotNull(savedAddress.id)
        assertNotEquals(0L, savedAddress.id)

        val foundAddress = orderAddressRepository.findById(savedAddress.id)
        assertNotNull(foundAddress)
        assertEquals(savedAddress.street, foundAddress?.street)
        assertEquals(savedAddress.zipCode, foundAddress?.zipCode)
    }

    @Test
    fun `should return null when address not found`() {
        val foundAddress = orderAddressRepository.findById(-1L)
        assertNull(foundAddress)
    }
}
