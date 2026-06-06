package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.CustomerAddress
import java.time.LocalDateTime

data class CustomerAddressDTO(
    var id: Long? = null,
    var primary: Boolean = false,
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
    constructor(address: CustomerAddress) : this() {
        this.id = address.id
        this.primary = address.primary
        this.zipCode = address.zipCode
        this.street = address.street
        this.number = address.number
        this.city = address.complement
        this.state = address.state
        this.createdAt = address.createdAt
        this.updatedAt = address.updatedAt
    }
}
