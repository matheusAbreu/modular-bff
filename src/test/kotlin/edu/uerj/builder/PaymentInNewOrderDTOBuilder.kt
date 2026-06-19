package edu.uerj.builder

import edu.uerj.adapters.api.dto.payment.PaymentInNewOrderDTO
import java.math.BigDecimal

class PaymentInNewOrderDTOBuilder(
    var method: String = "CREDIT_CARD",
    var installments: Int = 1,
    var installmentValue: BigDecimal = (10..100).random().toBigDecimal(),
    var discount: BigDecimal = BigDecimal.ZERO
) {
    fun build() = PaymentInNewOrderDTO(
        method = method,
        installments = installments,
        installmentValue = installmentValue,
        discount = discount
    )
}
