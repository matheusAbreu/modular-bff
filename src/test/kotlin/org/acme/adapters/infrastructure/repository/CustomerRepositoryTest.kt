package org.acme.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.domain.Customer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@QuarkusTest
@Transactional
class CustomerRepositoryTest {

    @Inject
    lateinit var customerRepository: CustomerRepository

    @Test
    fun `should save and find a customer`() {
        val testId = (1..10000).random()
        val customer = Customer(
            name = "Test User $testId",
            email = "test$testId@example.com",
            document = "123456789$testId",
            documentType = "CPF",
            birthDate = LocalDateTime.now().minusYears(20)
        )

        val savedCustomer = customerRepository.save(customer)

        assertNotNull(savedCustomer.id)
        assertNotEquals(0L, savedCustomer.id)
        assertNotNull(savedCustomer.createdAt)

        val foundCustomer = customerRepository.findById(savedCustomer.id)
        assertNotNull(foundCustomer)
        assertEquals(savedCustomer.name, foundCustomer?.name)
        assertEquals(savedCustomer.email, foundCustomer?.email)
    }

    @Test
    fun `should return null when customer not found`() {
        val foundCustomer = customerRepository.findById(-1L)
        assertNull(foundCustomer)
    }
}
