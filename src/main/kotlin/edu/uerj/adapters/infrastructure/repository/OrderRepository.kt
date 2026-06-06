package edu.uerj.adapters.infrastructure.repository

import edu.uerj.adapters.api.dto.PaginatedResult
import edu.uerj.domain.Order
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import java.sql.Date
import java.time.LocalDateTime

@ApplicationScoped
class OrderRepository : BaseRepository() {
    companion object {
        private const val NATIVE_ORDERS_COUNT = "SELECT COUNT(p.id) FROM Pedidos p"
        private const val SELECT_BASE = "SELECT o FROM Order o"
        private const val SELECT_BY_ID = "$SELECT_BASE WHERE o.id = :orderId"
        private const val ORDERNED_WITH_PAGINATION = "$SELECT_BASE ORDER BY o.createdAt DESC LIMIT :limit OFFSET :offset"
    }

    fun findById(id: Long): Order? =
        try {
            return getEntityManager()
                .createQuery(SELECT_BY_ID)
                .setParameter("orderId", id)
                .singleResult as Order
        } catch (e: NoResultException) {
            return null
        }

    fun listOrder(
        limit: Long = 50,
        offset: Long = 0,
    ): MutableList<Order> =
        try {
            return getEntityManager()
                    .createQuery(ORDERNED_WITH_PAGINATION)
                    .setParameter("limit", limit)
                    .setParameter("offset", (offset * limit))
                    .resultList as MutableList<Order>

        } catch (e: NoResultException) {
            return mutableListOf()
        }

    fun countOrders() = getEntityManager()
        .createNativeQuery(NATIVE_ORDERS_COUNT)
        .singleResult as Long


    fun save(order: Order): Order {
        order.updatedAt = LocalDateTime.now()
        val savedOrder = getEntityManager().merge(order)
        getEntityManager().flush()

        return savedOrder
    }
}
