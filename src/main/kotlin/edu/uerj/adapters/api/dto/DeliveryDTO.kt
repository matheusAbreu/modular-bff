package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.Delivery
import java.math.BigDecimal
import java.time.LocalDateTime

data class DeliveryDTO(
    var id: Long? = null,
    var contactPhone: String? = null,
    var contactName: String? = null,
    var freight: BigDecimal = BigDecimal.ZERO,
    var status: String? = null,
    var addressDestination: OrderAddressDTO? = null,
    var fiscalNoteDanfe: FiscalNoteDTO? = null,
    var items: List<OrderItemDTO>? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(delivery: Delivery) : this() {
        this.id = delivery.id
        this.contactPhone = delivery.contactPhone
        this.contactName = delivery.contactName
        this.freight = delivery.freight
        this.status = delivery.status
        this.createdAt = delivery.createdAt
        this.updatedAt = delivery.updatedAt

        if (delivery.address != null) {
            this.addressDestination = OrderAddressDTO(delivery.address!!)
        }

        if (delivery.fiscalNoteDanfe != null) {
            this.fiscalNoteDanfe = FiscalNoteDTO(delivery.fiscalNoteDanfe!!)
        }

        if (delivery.items.isNotEmpty()) {
            this.items = delivery.items.map { OrderItemDTO(it) }
        }
    }
}
