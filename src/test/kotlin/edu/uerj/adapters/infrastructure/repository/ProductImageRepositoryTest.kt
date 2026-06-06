package edu.uerj.adapters.infrastructure.repository

import edu.uerj.domain.Product
import edu.uerj.domain.ProductImage
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

@QuarkusTest
@Transactional
class ProductImageRepositoryTest {
    @Inject
    lateinit var productImageRepository: ProductImageRepository

    @Inject
    lateinit var productRepository: ProductRepository

    @Test
    fun `should save and find a product image`() {
        val testId = (1..10000).random()

        val product =
            Product().apply {
                this.sku = "PI-SKU-$testId"
                this.name = "Test Product for Image"
                this.description = "Description"
                this.price = BigDecimal.valueOf(10.0)
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }
        val savedProduct = productRepository.save(product)

        val image =
            ProductImage(
                product = savedProduct,
                base64 = "YmFzZTY0ZGF0YQ==", // "base64data"
            )

        val savedImage = productImageRepository.save(image)

        assertNotNull(savedImage.id)
        assertNotEquals(0L, savedImage.id)

        val foundImage = productImageRepository.findById(savedImage.id)
        assertNotNull(foundImage)
        assertEquals(savedImage.base64, foundImage?.base64)
        assertEquals(savedProduct.sku, foundImage?.product?.sku)
    }

    @Test
    fun `should return null when image not found`() {
        val foundImage = productImageRepository.findById(-1L)
        assertNull(foundImage)
    }
}
