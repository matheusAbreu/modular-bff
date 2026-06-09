package edu.uerj.adapters.api.dto.order

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.adapters.api.dto.CustomerDTO
import edu.uerj.adapters.api.dto.DeliveryDTO
import edu.uerj.adapters.api.dto.payment.PaymentDTO
import edu.uerj.domain.Order
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderCompleteDTO(
    var id: Long? = null,
    var customer: CustomerDTO? = null,
    var status: String? = null,
    var total: BigDecimal = BigDecimal.ZERO,
    var discount: BigDecimal = BigDecimal.ZERO,
    var freight: BigDecimal = BigDecimal.ZERO,
    var deliverys: List<DeliveryDTO>? = null,
    var payments: List<PaymentDTO>? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(order: Order) : this() {
        this.id = order.id
        this.status = order.status
        this.total = order.total
        this.discount = order.discount
        this.freight = order.freight
        this.createdAt = order.createdAt
        this.updatedAt = order.updatedAt

        if (order.customer != null) {
            this.customer = CustomerDTO(order.customer!!)
        }

        if (order.deliverys.isNotEmpty()) {
            this.deliverys = order.deliverys.map { DeliveryDTO(it) }
        }

        if (order.payments.isNotEmpty()) {
            this.payments = order.payments.map { PaymentDTO(it) }
        }
    }
}
