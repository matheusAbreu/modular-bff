package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.OrderAddress
import java.time.LocalDateTime

data class OrderAddressDTO(
    var id: Long? = null,
    var zipCode: String? = null,
    var street: String? = null,
    var number: String? = null,
    var complement: String? = null,
    var city: String? = null,
    var state: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(orderAddress: OrderAddress) : this() {
        this.id = orderAddress.id
        this.zipCode = orderAddress.zipCode
        this.street = orderAddress.street
        this.number = orderAddress.number
        this.complement = orderAddress.complement
        this.city = orderAddress.city
        this.state = orderAddress.state
        this.createdAt = orderAddress.createdAt
        this.updatedAt = orderAddress.updatedAt
    }
}
