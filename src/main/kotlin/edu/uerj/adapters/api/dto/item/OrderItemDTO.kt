package edu.uerj.adapters.api.dto.item

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.adapters.api.dto.ProductDTO
import edu.uerj.domain.OrderItem
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderItemDTO(
    var id: Long? = null,
    var product: ProductDTO? = null,
    var quantity: Int = 0,
    var unitPrice: BigDecimal = BigDecimal.ZERO,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(orderItem: OrderItem) : this() {
        this.id = orderItem.id
        this.quantity = orderItem.quantity
        this.unitPrice = orderItem.unitPrice
        this.totalPrice = orderItem.totalPrice
        this.createdAt = orderItem.createdAt
        this.updatedAt = orderItem.updatedAt

        if (orderItem.product != null) {
            this.product = ProductDTO(orderItem.product!!)
        }
    }
}
