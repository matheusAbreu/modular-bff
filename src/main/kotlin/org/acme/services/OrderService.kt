package org.acme.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.acme.adapters.infrastructure.repository.OrderRepository
import org.acme.domain.Order

@ApplicationScoped
class OrderService
@Inject constructor(
  private val orderRepository: OrderRepository
) {

    fun getOrder(orderId: Long): Order? {
        return orderRepository.findById(orderId)
    }
}