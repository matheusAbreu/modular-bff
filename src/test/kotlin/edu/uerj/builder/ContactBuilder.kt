package edu.uerj.builder

import edu.uerj.domain.Contact
import edu.uerj.domain.Customer
import java.time.LocalDateTime

class ContactBuilder(
    var id: Long? = (1..100).random().toLong(),
    var customer: Customer? = null,
    var primary: Boolean = false,
    var contactType: String? = "EMAIL",
    var value: String? = "contact${(1..100).random()}@example.com",
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun build(): Contact {
        val contact = Contact()
        contact.id = id ?: 0
        contact.customer = customer
        contact.primary = primary
        contact.contactType = contactType
        contact.value = value
        contact.createdAt = createdAt
        contact.updatedAt = updatedAt
        return contact
    }
}
