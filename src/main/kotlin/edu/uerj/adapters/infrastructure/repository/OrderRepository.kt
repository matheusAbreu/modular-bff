package edu.uerj.adapters.infrastructure.repository

import edu.uerj.adapters.api.dto.PaginatedResult
import edu.uerj.domain.Order
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import java.time.LocalDateTime

@ApplicationScoped
class OrderRepository : BaseRepository() {
    companion object {
        private const val SELECT_BY_ID = "SELECT o FROM Order o WHERE o.id = :orderId"
        private const val ORDERNED_WITH_PAGINATION =
            "SELECT o, ROW_NUMBER() OVER (ORDER BY o.createdAt DESC) FROM Order o" +
//                "COUNT(*) OVER() AS TotalCount, " +
//                "ROW_NUMBER() OVER (ORDER BY a.created_at desc) AS RowNum" +
                " ORDER BY o.createdAt DESC LIMIT :limit OFFSET :offset"
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
        limit: Int = 50,
        offset: Int = 0,
    ): PaginatedResult<Order> =
        try {
            var limit = 0
            var offset = 0
            var convertedRows = mutableListOf<Order>()
            var totalRowns = 0
            val result =
                getEntityManager()
                    .createQuery(ORDERNED_WITH_PAGINATION)
                    .setParameter("limit", limit)
                    .setParameter("offset", offset)
                    .resultList

            return PaginatedResult(
                items = result as MutableList<Order>,
                limit = limit,
                offset = offset,
                total = result.count(),
            )
        } catch (e: NoResultException) {
            return PaginatedResult()
        }

    fun save(order: Order): Order {
        order.updatedAt = LocalDateTime.now()
        val savedOrder = getEntityManager().merge(order)
        getEntityManager().flush()

        return savedOrder
    }
}
