package edu.uerj.adapters.api.dto.payment

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.Payment
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentDTO(
    var id: Long? = null,
    var method: String? = null,
    var installments: Int = 0,
    var installmentValue: BigDecimal = BigDecimal.ZERO,
    var totalValue: BigDecimal = BigDecimal.ZERO,
    var status: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(payment: Payment) : this() {
        this.id = payment.id
        this.method = payment.method
        this.installments = payment.installments
        this.installmentValue = payment.installmentValue
        this.totalValue = payment.totalValue
        this.status = payment.status
        this.createdAt = payment.createdAt
        this.updatedAt = payment.updatedAt
    }
}