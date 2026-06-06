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
            private val LIMIT_DEFAULT: Long = 50
            private val OFFSET_DEFAULT: Long = 0
        }

        fun getOrder(orderId: Long): Order? = orderRepository.findById(orderId)

        fun listAll(
            limit: Long,
            offset: Long,
        ): PaginatedResult<Order> {
            var internalLimit = LIMIT_DEFAULT
            var internalOffset = OFFSET_DEFAULT

            if (limit > 1 || limit < 500) {
                internalLimit = limit
            }

            if (offset > 0) {
                internalOffset = offset
            }

            return PaginatedResult(
                items = orderRepository.listOrder(internalLimit, internalOffset),
                limit = limit,
                total = orderRepository.countOrders(),
            )
        }
    }
