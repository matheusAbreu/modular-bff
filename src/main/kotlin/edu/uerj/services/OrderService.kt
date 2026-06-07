package edu.uerj.services

import edu.uerj.adapters.api.dto.PaginatedResult
import edu.uerj.adapters.infrastructure.repository.OrderRepository
import edu.uerj.domain.Order
import edu.uerj.domain.enums.OrderStatus
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@Transactional
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

        fun updateStatus(orderId: Long, deliveryId: Long, status: OrderStatus): Order? {
            val internalOrder = orderRepository.findById(orderId) ?: return null
            val deliveryTarget = internalOrder.deliverys.firstOrNull { it.id == deliveryId } ?: return null

            val orderCurrentStatus = OrderStatus.getByDescription(deliveryTarget.status)
            if (orderCurrentStatus.id < status.id) {
                deliveryTarget.status = status.description
                internalOrder.checkOrderStatus()
                return orderRepository.save(internalOrder)
            }

            throw IllegalArgumentException("Delivery with id '${deliveryTarget.id}' in Order with id ${internalOrder.id}" +
                    " cannot be updated to status '${status.description}' " +
                    "because it is already in status '${orderCurrentStatus.description}'.")
        }
    }
