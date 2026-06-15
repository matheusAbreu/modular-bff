package edu.uerj.builder

import edu.uerj.domain.Customer
import edu.uerj.domain.CustomerAddress
import java.time.LocalDateTime

class CustomerAddressBuilder(
    var id: Long? = (1..100).random().toLong(),
    var customer: Customer? = null,
    var primary: Boolean = false,
    var zipCode: String? = "12345-678",
    var street: String? = "Main St",
    var number: String? = "123",
    var complement: String? = "Apt 1",
    var city: String? = "City",
    var state: String? = "State",
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun build(): CustomerAddress {
        val address = CustomerAddress()
        address.id = id ?: 0
        address.customer = customer
        address.primary = primary
        address.zipCode = zipCode
        address.street = street
        address.number = number
        address.complement = complement
        address.city = city
        address.state = state
        address.createdAt = createdAt
        address.updatedAt = updatedAt
        return address
    }
}
