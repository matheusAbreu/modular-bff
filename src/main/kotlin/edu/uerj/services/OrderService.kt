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
            private const val LIMIT_DEFAULT: Long = 50
            private const val OFFSET_DEFAULT: Long = 0
            private const val MINIMAL_QUANTITY_PER_PAGE: Long = 1
            private const val MAXIMAL_QUANTITY_PER_PAGE: Long = 500
        }

        fun getOrder(orderId: Long): Order? = orderRepository.findById(orderId)

        fun listAll(
            limit: Long,
            offset: Long,
        ): PaginatedResult<Order> {
            var internalLimit = LIMIT_DEFAULT
            var internalOffset = OFFSET_DEFAULT

            if (limit in MINIMAL_QUANTITY_PER_PAGE..MAXIMAL_QUANTITY_PER_PAGE) {
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

        @Suppress("MagicNumber")
        fun createOrder(newOrder: NewOrderDTO): Order {
            val customer =
                customerRepository.findById(newOrder.customerId)
                    ?: throw IllegalArgumentException("Customer with id ${newOrder.customerId} not found.")

            val address =
                customer.addresses.firstOrNull { it.id == newOrder.addressId }
                    ?: throw IllegalArgumentException(
                        "Address with id ${newOrder.addressId} not found for customer ${newOrder.customerId}.",
                    )

            val contact =
                customer.contacts.firstOrNull { it.id == newOrder.contactId } ?: throw IllegalArgumentException(
                    "Contact with id ${newOrder.contactId} not found for customer ${newOrder.customerId}.",
                )

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

                    check(
                        it.unitPrice == product.price,
                    ) { "The price of the product with SKU '${product.sku}' does not match the price in the database." }

                    OrderItem(
                        product,
                        order,
                        null,
                        it.quantity,
                        product.price,
                        BigDecimal(it.quantity) * product.price,
                    )
                }

            items.chunked(5).forEach { itemsToDelivery ->
                val delivery =
                    Delivery(
                        order,
                        orderAddress,
                        contact.value,
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
