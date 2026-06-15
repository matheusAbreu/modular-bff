package edu.uerj.adapters.infrastructure.repository

import edu.uerj.domain.ProductImage
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import java.time.LocalDateTime

@ApplicationScoped
class ProductImageRepository : BaseRepository() {
    companion object {
        private const val SELECT_BY_ID = "SELECT i FROM ProductImage i WHERE i.id = :imageId"
    }

    fun findById(id: Long): ProductImage? =
        try {
            getEntityManager()
                .createQuery(SELECT_BY_ID)
                .setParameter("imageId", id)
                .singleResult as ProductImage
        } catch (e: NoResultException) {
            null
        }

    fun save(image: ProductImage): ProductImage {
        image.updatedAt = LocalDateTime.now()
        val savedImage = getEntityManager().merge(image)
        getEntityManager().flush()
        return savedImage
    }
}
