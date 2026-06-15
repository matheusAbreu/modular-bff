package edu.uerj.adapters.api.dto.item

import java.math.BigDecimal

data class ItemInNewOrderDTO(
    var productSku: String = "",
    var quantity: Int = 0,
    var unitPrice: BigDecimal = BigDecimal.ZERO,
)
