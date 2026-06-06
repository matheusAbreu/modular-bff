package edu.uerj.services

import edu.uerj.adapters.api.dto.PaginatedResult
import edu.uerj.adapters.infrastructure.repository.OrderRepository
import edu.uerj.domain.Order
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class OrderService
    @Inject
    constructor(
        private val orderRepository: OrderRepository,
    ) {
        companion object {
            private val LIMIT_DEFAULT = 50
            private val OFFSET_DEFAULT = 0
        }

        fun getOrder(orderId: Long): Order? = orderRepository.findById(orderId)

        fun listAll(
            limit: Int,
            offset: Int,
        ): PaginatedResult<Order> {
            var internalLimit = LIMIT_DEFAULT
            var internalOffset = OFFSET_DEFAULT

            if (limit > 1 || limit < 500) {
                internalLimit = limit
            }

            if (offset > 0) {
                internalOffset = offset
            }

            val paginatedOrders = orderRepository.listOrder(internalLimit, internalOffset)
            return paginatedOrders
        }
    }
