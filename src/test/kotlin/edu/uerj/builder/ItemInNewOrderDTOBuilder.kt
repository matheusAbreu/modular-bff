package edu.uerj.builder

import edu.uerj.adapters.api.dto.item.ItemInNewOrderDTO
import java.math.BigDecimal

class ItemInNewOrderDTOBuilder(
    var productSku: String = "SKU-${(1..1000).random()}",
    var quantity: Int = (1..10).random(),
    var unitPrice: BigDecimal = (10..100).random().toBigDecimal(),
) {
    fun build() =
        ItemInNewOrderDTO(
            productSku = productSku,
            quantity = quantity,
            unitPrice = unitPrice,
        )
}
