package edu.uerj.builder

import edu.uerj.domain.Delivery
import edu.uerj.domain.FiscalNote
import edu.uerj.domain.Order
import edu.uerj.domain.OrderAddress
import edu.uerj.domain.OrderItem
import edu.uerj.domain.enums.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

class DeliveryBuilder(
    var id: Long? = (1..100).random().toLong(),
    var order: Order? = null,
    var address: OrderAddress? = null,
    var fiscalNoteDanfe: FiscalNote? = null,
    var contactPhone: String? = "11999999999",
    var contactName: String? = "Contact ${(1..100).random()}",
    var freight: BigDecimal = (1..100).random().toBigDecimal(),
    var status: String = OrderStatus.Companion.getById((0..8).random().toLong()).description,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    val items: MutableList<OrderItem> = mutableListOf(),
) {
    fun build(): Delivery {
        val delivery = Delivery()
        delivery.id = id ?: 0
        delivery.order = order
        delivery.address = address
        delivery.fiscalNoteDanfe = fiscalNoteDanfe
        delivery.contactPhone = contactPhone
        delivery.contactName = contactName
        delivery.freight = freight
        delivery.status = status
        delivery.createdAt = createdAt
        delivery.updatedAt = updatedAt
        delivery.items.addAll(items)
        return delivery
    }
}
