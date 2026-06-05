package edu.uerj.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import edu.uerj.domain.Product
import java.time.LocalDateTime


@ApplicationScoped
class ProductRepository: BaseRepository() {

    companion object {
        private const val SELECT_BY_SKU = "SELECT p FROM Product p WHERE p.sku = :sku"
    }

    fun findBySku(sku: String): Product? = try {
        getEntityManager()
            .createQuery(SELECT_BY_SKU)
            .setParameter("sku", sku)
            .singleResult as Product
    } catch (e: NoResultException) {
        null
    }

    fun save(product: Product): Product {
        product.updatedAt = LocalDateTime.now()
        val savedProduct = getEntityManager().merge(product)
        getEntityManager().flush()
        return savedProduct
    }
}
