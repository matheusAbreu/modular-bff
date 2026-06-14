package edu.uerj.builder

import edu.uerj.domain.Contact
import edu.uerj.domain.Customer
import edu.uerj.domain.CustomerAddress
import edu.uerj.domain.Order
import java.time.LocalDateTime

class CustomerBuilder(
    var id: Long? = (1..100).random().toLong(),
    var name: String? = "Customer ${(1..100).random()}",
    var email: String? = "customer${(1..100).random()}@example.com",
    var document: String? = "12345678901",
    var documentType: String? = "CPF",
    var birthDate: LocalDateTime? = LocalDateTime.now().minusYears(20),
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    val contacts: MutableList<Contact> = mutableListOf(),
    val addresses: MutableList<CustomerAddress> = mutableListOf(),
    val orders: MutableList<Order> = mutableListOf(),
) {
    fun build(): Customer {
        val customer = Customer()
        customer.id = id ?: 0
        customer.name = name
        customer.email = email
        customer.document = document
        customer.documentType = documentType
        customer.birthDate = birthDate
        customer.createdAt = createdAt
        customer.updatedAt = updatedAt
        customer.contacts.addAll(contacts)
        customer.addresses.addAll(addresses)
        customer.orders.addAll(orders)
        return customer
    }
}

