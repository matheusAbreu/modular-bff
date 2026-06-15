package edu.uerj.builder

import edu.uerj.domain.Customer
import edu.uerj.domain.Delivery
import edu.uerj.domain.Order
import edu.uerj.domain.Payment
import edu.uerj.domain.enums.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderBuilder(
    var id: Long? = (1..100).random().toLong(),
    var customer: Customer? = null,
    var status: String = OrderStatus.getById((0..8).random().toLong()).description,
    var total: BigDecimal =
        (1..100)
            .random()
            .toBigDecimal()
            .add(
                (1..100).random().div(100).toBigDecimal(),
            ),
    var discount: BigDecimal =
        (1..100)
            .random()
            .toBigDecimal()
            .add(
                (1..100).random().div(100).toBigDecimal(),
            ),
    var freight: BigDecimal =
        (1..100)
            .random()
            .toBigDecimal()
            .add(
                (1..100).random().div(100).toBigDecimal(),
            ),
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    val deliverys: MutableList<Delivery> = mutableListOf(),
    val payments: MutableList<Payment> = mutableListOf(),
) {
    fun build(): Order {
        var order = Order()
        order.id = id
        order.customer = customer
        order.status = status
        order.total = total
        order.discount = discount
        order.freight = freight
        order.createdAt = createdAt
        order.updatedAt = updatedAt
        order.deliverys.addAll(deliverys)
        order.payments.addAll(payments)

        return order
    }
}
