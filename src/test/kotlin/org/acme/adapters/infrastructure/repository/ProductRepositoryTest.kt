package org.acme.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.domain.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import java.math.BigDecimal
import org.junit.jupiter.api.Test

@QuarkusTest
@Transactional
class ProductRepositoryTest {

    @Inject
    lateinit var productRepository: ProductRepository

    @Test
    fun `should save and find a product`() {
        val testId = (1..10000).random()
        val product = Product(
            sku = "SKU-$testId",
            name = "Test Product $testId",
            description = "Description for product $testId",
            price = BigDecimal.valueOf(99.99)
        )

        val savedProduct = productRepository.save(product)

        assertEquals(product.sku, savedProduct.sku)
        assertNotNull(savedProduct.createdAt)

        val foundProduct = productRepository.findBySku(savedProduct.sku)
        assertNotNull(foundProduct)
        assertEquals(savedProduct.name, foundProduct?.name)
        assertEquals(savedProduct.price, foundProduct?.price)
    }

    @Test
    fun `should return null when product not found`() {
        val foundProduct = productRepository.findBySku("NON-EXISTENT")
        assertNull(foundProduct)
    }
}
