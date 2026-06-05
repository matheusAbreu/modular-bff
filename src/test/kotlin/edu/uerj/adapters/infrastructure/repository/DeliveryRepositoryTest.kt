package edu.uerj.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import edu.uerj.domain.Delivery
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import java.math.BigDecimal
import org.junit.jupiter.api.Test

@QuarkusTest
@Transactional
class DeliveryRepositoryTest {

    @Inject
    lateinit var deliveryRepository: DeliveryRepository

    @Test
    fun `should save and find a delivery`() {
        val testId = (1..10000).random()
        val delivery = Delivery(
            order = null,
            address = null,
            contactPhone = "12345678",
            contactName = "Contact $testId",
            freight = BigDecimal.valueOf(15.0),
            status = "PENDING"
        )

        val savedDelivery = deliveryRepository.save(delivery)

        assertNotNull(savedDelivery.id)
        assertNotEquals(0L, savedDelivery.id)

        val foundDelivery = deliveryRepository.findById(savedDelivery.id)
        assertNotNull(foundDelivery)
        assertEquals(savedDelivery.contactName, foundDelivery?.contactName)
        assertEquals(savedDelivery.status, foundDelivery?.status)
    }

    @Test
    fun `should return null when delivery not found`() {
        val foundDelivery = deliveryRepository.findById(-1L)
        assertNull(foundDelivery)
    }
}
