package edu.uerj.adapters.infrastructure.repository

import edu.uerj.domain.Customer
import edu.uerj.domain.CustomerAddress
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@QuarkusTest
@Transactional
class CustomerAddressRepositoryTest {
    @Inject
    lateinit var customerAddressRepository: CustomerAddressRepository

    @Test
    fun `should save and find a customer address`() {
        val testId = (1..10000).random()
        val customer =
            Customer(
                name = "Address Owner $testId",
                email = "owner$testId@example.com",
                document = "DOC$testId",
                documentType = "CPF",
                birthDate = LocalDateTime.now().minusYears(25),
            )

        val address =
            CustomerAddress(
                customer = customer,
                primary = true,
                zipCode = "12345-678",
                street = "Test Street $testId",
                number = "123",
                complement = null,
                city = "Test City",
                state = "TS",
            )

        val savedAddress = customerAddressRepository.save(address)

        assertNotNull(savedAddress.id)
        assertNotEquals(0L, savedAddress.id)
        assertNotNull(savedAddress.customer?.id)

        val foundAddress = customerAddressRepository.findById(savedAddress.id)
        assertNotNull(foundAddress)
        assertEquals(savedAddress.street, foundAddress?.street)
        assertEquals(savedAddress.city, foundAddress?.city)
    }

    @Test
    fun `should return null when address not found`() {
        val foundAddress = customerAddressRepository.findById(-1L)
        assertNull(foundAddress)
    }
}
