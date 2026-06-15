package edu.uerj.builder

import edu.uerj.domain.Order
import edu.uerj.domain.Payment
import java.math.BigDecimal
import java.time.LocalDateTime

class PaymentsBuilder(
    var id: Long? = (1..100).random().toLong(),
    var order: Order? = null,
    var method: String? = "CREDIT_CARD",
    var installments: Int = (1..12).random(),
    var installmentValue: BigDecimal = (10..100).random().toBigDecimal(),
    var totalValue: BigDecimal = (100..1000).random().toBigDecimal(),
    var status: String? = "PAID",
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun build(): Payment {
        val payment = Payment()
        payment.id = id ?: 0
        payment.order = order
        payment.method = method
        payment.installments = installments
        payment.installmentValue = installmentValue
        payment.totalValue = totalValue
        payment.status = status
        payment.createdAt = createdAt
        payment.updatedAt = updatedAt
        return payment
    }
}
