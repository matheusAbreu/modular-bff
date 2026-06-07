package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.Product
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductDTO(
    var sku: String = "",
    var name: String? = null,
    var description: String? = null,
    var price: BigDecimal = BigDecimal.ZERO,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(product: Product) : this() {
        this.sku = product.sku
        this.name = product.name
        this.description = product.description
        this.price = product.price
        this.createdAt = product.createdAt
        this.updatedAt = product.updatedAt
    }
}
