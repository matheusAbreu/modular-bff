package edu.uerj.adapters.api.dto.payment

import java.math.BigDecimal

data class PaymentInNewOrderDTO(
    var method: String? = null,
    var installments: Int = 0,
    var installmentValue: BigDecimal = BigDecimal.ZERO,
    var discount: BigDecimal = BigDecimal.ZERO,
)
