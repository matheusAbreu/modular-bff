package org.acme.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import org.acme.domain.OrderItem
import java.time.LocalDateTime


@ApplicationScoped
class OrderItemRepository: BaseRepository() {

    companion object {
        private const val SELECT_BY_ID = "SELECT i FROM OrderItem i WHERE i.id = :itemId"
    }

    fun findById(id: Long): OrderItem? = try {
        getEntityManager()
            .createQuery(SELECT_BY_ID)
            .setParameter("itemId", id)
            .singleResult as OrderItem
    } catch (e: NoResultException) {
        null
    }

    fun save(item: OrderItem): OrderItem {
        item.updatedAt = LocalDateTime.now()
        val savedItem = getEntityManager().merge(item)
        getEntityManager().flush()
        return savedItem
    }
}
