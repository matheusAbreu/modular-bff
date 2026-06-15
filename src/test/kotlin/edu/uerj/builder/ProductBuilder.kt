package edu.uerj.builder

import edu.uerj.domain.Product
import java.math.BigDecimal
import java.time.LocalDateTime

class ProductBuilder(
    var sku: String = "SKU-${(1000..9999).random()}",
    var name: String? = "Product ${(1..100).random()}",
    var description: String? = "Description for product",
    var price: BigDecimal = (10..100).random().toBigDecimal(),
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun build(): Product {
        val product = Product()
        product.sku = sku
        product.name = name
        product.description = description
        product.price = price
        product.createdAt = createdAt
        product.updatedAt = updatedAt
        return product
    }
}
