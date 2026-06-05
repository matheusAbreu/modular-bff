package edu.uerj.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import edu.uerj.domain.Payment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import java.math.BigDecimal
import org.junit.jupiter.api.Test

@QuarkusTest
@Transactional
class PaymentRepositoryTest {

    @Inject
    lateinit var paymentRepository: PaymentRepository

    @Test
    fun `should save and find a payment`() {
        val testId = (1..10000).random()
        val payment = Payment(
            order = null,
            method = "CREDIT_CARD",
            installments = 3,
            installmentValue = BigDecimal.valueOf(33.33),
            totalValue = BigDecimal.valueOf(100.0),
            status = "APPROVED"
        )

        val savedPayment = paymentRepository.save(payment)

        assertNotNull(savedPayment.id)
        assertNotEquals(0L, savedPayment.id)

        val foundPayment = paymentRepository.findById(savedPayment.id)
        assertNotNull(foundPayment)
        assertEquals(savedPayment.method, foundPayment?.method)
        assertEquals(savedPayment.totalValue, foundPayment?.totalValue)
    }

    @Test
    fun `should return null when payment not found`() {
        val foundPayment = paymentRepository.findById(-1L)
        assertNull(foundPayment)
    }
}
