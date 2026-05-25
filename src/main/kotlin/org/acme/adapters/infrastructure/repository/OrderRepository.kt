package org.acme.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import jakarta.transaction.Transactional
import org.acme.domain.Order
import java.time.LocalDateTime


@ApplicationScoped
class OrderRepository: BaseRepository() {

    companion object {
            private const val SELECT_BY_ID = "SELECT o FROM Order o WHERE o.id = :orderId"
    }

    fun findById(id: Long): Order? = try {
            return getEntityManager()
                .createQuery(SELECT_BY_ID)
                .setParameter("orderId", id)
                .singleResult as Order
        } catch (e: NoResultException) {
            return null
        }

    
    fun save(order: Order): Order {
        order.updatedAt = LocalDateTime.now()
        val savedOrder = getEntityManager().merge(order)
        getEntityManager().flush()
        
        return savedOrder
    }
}