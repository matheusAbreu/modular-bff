package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.Customer
import java.time.LocalDateTime

data class CustomerDTO (
    var id: Long? = null,
    var name: String? = null,
    var email: String? = null,
    var document: String? = null,
    var documentType: String? = null,
        
    @JsonFormat(pattern = "yyyy-MM-dd")
    var birthDate: LocalDateTime? = null,
    
     var contacts: List<ContactDTO>? = null,
     var addresses: List<CustomerAddressDTO>? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null
) {
    constructor(customer: Customer) : this() {
        this.id = customer.id
        this.name = customer.name
        this.email = customer.email
        this.document = customer.document
        this.documentType = customer.documentType
        this.birthDate = customer.birthDate
        this.createdAt = customer.createdAt
        this.updatedAt = customer.updatedAt

        if(customer.contacts.isNotEmpty()) {
            this.contacts = customer.contacts.map{ ContactDTO(it) }
        }
        if(customer.addresses.isNotEmpty()) {
            this.addresses = customer.addresses.map { CustomerAddressDTO(it) }
        }

    }
}