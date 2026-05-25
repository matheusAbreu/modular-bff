package org.acme.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.domain.Contact
import org.acme.domain.Customer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@QuarkusTest
@Transactional
class ContactRepositoryTest {

    @Inject
    lateinit var contactRepository: ContactRepository

    @Test
    fun `should save and find a contact`() {
        val testId = (1..10000).random()
        val customer = Customer(
            name = "Contact Owner $testId",
            email = "owner$testId@example.com",
            document = "DOC$testId",
            documentType = "CPF",
            birthDate = LocalDateTime.now().minusYears(30)
        )

        val contact = Contact(
            customer = customer,
            primary = true,
            contactType = "EMAIL",
            value = "test$testId@example.com"
        )

        val savedContact = contactRepository.save(contact)

        assertNotNull(savedContact.id)
        assertNotEquals(0L, savedContact.id)
        assertNotNull(savedContact.customer?.id)

        val foundContact = contactRepository.findById(savedContact.id)
        assertNotNull(foundContact)
        assertEquals(savedContact.value, foundContact?.value)
        assertEquals(savedContact.contactType, foundContact?.contactType)
    }

    @Test
    fun `should return null when contact not found`() {
        val foundContact = contactRepository.findById(-1L)
        assertNull(foundContact)
    }
}
