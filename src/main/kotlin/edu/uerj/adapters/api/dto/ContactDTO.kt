package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.Contact
import java.time.LocalDateTime

data class ContactDTO(
    var id: Long? = null,
    var primary: Boolean = false,
    var contactType: String? = null,
    var value: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(contact: Contact) : this() {
        this.id = contact.id
        this.primary = contact.primary
        this.value = contact.value
        this.createdAt = contact.createdAt
        this.updatedAt = contact.updatedAt
        this.contactType = contact.contactType
        this.primary = contact.primary
    }
}
