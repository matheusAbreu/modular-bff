package edu.uerj.services

import edu.uerj.adapters.api.dto.common.PaginatedResult
import edu.uerj.adapters.api.dto.order.NewOrderDTO
import edu.uerj.adapters.infrastructure.repository.CustomerRepository
import edu.uerj.adapters.infrastructure.repository.OrderRepository
import edu.uerj.adapters.infrastructure.repository.ProductRepository
import edu.uerj.domain.Delivery
import edu.uerj.domain.Order
import edu.uerj.domain.OrderAddress
import edu.uerj.domain.OrderItem
import edu.uerj.domain.Payment
import edu.uerj.domain.enums.OrderStatus
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import java.math.BigDecimal

@Transactional
@ApplicationScoped
class OrderService
    @Inject
    constructor(
        private val orderRepository: OrderRepository,
        private val customerRepository: CustomerRepository,
        private val productRepository: ProductRepository,
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

        fun updateStatus(
            orderId: Long,
            deliveryId: Long,
            status: OrderStatus,
        ): Order? {
            val internalOrder = orderRepository.findById(orderId) ?: return null
            val deliveryTarget = internalOrder.deliverys.firstOrNull { it.id == deliveryId } ?: return null

            val orderCurrentStatus = OrderStatus.getByDescription(deliveryTarget.status)
            if (orderCurrentStatus.id < status.id) {
                deliveryTarget.status = status.description
                internalOrder.checkOrderStatus()
                return orderRepository.save(internalOrder)
            }

            throw IllegalArgumentException(
                "Delivery with id '${deliveryTarget.id}' in Order with id ${internalOrder.id}" +
                    " cannot be updated to status '${status.description}' " +
                    "because it is already in status '${orderCurrentStatus.description}'.",
            )
        }

        fun createOrder(newOrder: NewOrderDTO): Order {
            var totalValue = BigDecimal.ZERO
            val customer =
                customerRepository.findById(newOrder.customerId)
                    ?: throw IllegalArgumentException("Customer with id ${newOrder.customerId} not found.")

            val address =
                customer.addresses.firstOrNull { it.id == newOrder.addressId }
                    ?: throw IllegalArgumentException("Customer with id ${newOrder.addressId} not found.")

            val contact = customer.contacts.firstOrNull { it.id == newOrder.contactId }

            val order =
                Order(
                    OrderStatus.NEW_ORDER.description,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    customer,
                )

            val orderAddress =
                OrderAddress(
                    customer,
                    order,
                    address.zipCode,
                    address.street,
                    address.number,
                    address.complement,
                    address.city,
                    address.state,
                )

            val items =
                newOrder.items.map {
                    val product =
                        productRepository.findBySku(it.productSku)
                            ?: throw IllegalArgumentException("Product with sku ${it.productSku} not found.")

                    OrderItem(
                        product,
                        order,
                        null,
                        it.quantity,
                        it.unitPrice,
                        BigDecimal(it.quantity) * it.unitPrice,
                    )
                }

            items.chunked(5).forEach { itemsToDelivery ->
                val delivery =
                    Delivery(
                        order,
                        orderAddress,
                        contact?.value,
                        customer.name,
                        (1..100).random().toBigDecimal(),
                        OrderStatus.NEW_ORDER.description,
                    )

                itemsToDelivery.forEach {
                    it.delivery = delivery
                    delivery.addItem(it)
                }

                order.deliverys.add(delivery)
            }

            newOrder.paymentMethods.forEach {
                val totalMethodValue = it.installmentValue * BigDecimal(it.installments)
                order.discount += it.discount
                order.total += totalMethodValue

                order.payments.add(
                    Payment(
                        order,
                        it.method,
                        it.installments,
                        it.installmentValue,
                        totalMethodValue,
                        OrderStatus.WAITING_PAYMENT.description,
                    ),
                )
            }

            return orderRepository.save(order)
        }
    }
