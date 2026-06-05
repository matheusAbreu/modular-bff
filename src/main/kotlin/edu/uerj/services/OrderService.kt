package edu.uerj.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import edu.uerj.adapters.infrastructure.repository.OrderRepository
import edu.uerj.domain.Order

@ApplicationScoped
class OrderService
@Inject constructor(
  private val orderRepository: OrderRepository
) {

    fun getOrder(orderId: Long): Order? {
        return orderRepository.findById(orderId)
    }
}