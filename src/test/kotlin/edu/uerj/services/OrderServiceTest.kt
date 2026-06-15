package edu.uerj.services

import edu.uerj.adapters.api.dto.item.ItemInNewOrderDTO
import edu.uerj.adapters.api.dto.order.NewOrderDTO
import edu.uerj.adapters.api.dto.payment.PaymentInNewOrder
import edu.uerj.adapters.infrastructure.repository.CustomerRepository
import edu.uerj.adapters.infrastructure.repository.OrderRepository
import edu.uerj.adapters.infrastructure.repository.ProductRepository
import edu.uerj.builder.CustomerBuilder
import edu.uerj.builder.DeliveryBuilder
import edu.uerj.builder.OrderBuilder
import edu.uerj.builder.ProductBuilder
import edu.uerj.domain.Order
import edu.uerj.domain.enums.OrderStatus
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import io.mockk.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.`when`
import java.math.BigDecimal

@QuarkusTest
class OrderServiceTest {
    lateinit var orderService: OrderService

    private val orderRepository: OrderRepository = mockk(relaxed = true)
    private val customerRepository: CustomerRepository = mockk(relaxed = true)
    private val productRepository: ProductRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        orderService = OrderService(
            orderRepository,
            customerRepository,
            productRepository
        )
    }


    @Test
    fun `should return order when it exists`() {
        val order = OrderBuilder().build()

        every { orderRepository.findById(order.id!!) } returns order

        val result = orderService.getOrder(order.id!!)

        assertEquals(order, result)
        assertEquals(order.id, result?.id)
    }

    @Test
    fun `should return null when order does not exist`() {
        every { orderRepository.findById(any()) } returns null

        val result = orderService.getOrder((1..1000).random().toLong())

        assertNull(result)
    }

    @Test
    fun `should list all orders with pagination`() {
        val orders = mutableListOf(OrderBuilder().build(), OrderBuilder().build())
        every { orderRepository.listOrder(any(), any()) } returns orders
        every { orderRepository.countOrders() } returns orders.count().toLong()

        val result = orderService.listAll(10, 0)

        assertEquals(2, result.items!!.size)
        assertEquals(2, result.total)
    }

    @Test
    fun `should update order status when valid`() {
        val delivery = DeliveryBuilder(
            status = OrderStatus.NEW_ORDER.description
        ).build()
        val order = OrderBuilder().build()
        order.deliverys.add(delivery)
        delivery.order = order

        every { orderRepository.findById(order.id!!) } returns order
        every { orderRepository.save(any())} returns order

        val result = orderService.updateStatus(order.id!!, delivery.id, OrderStatus.PAID)

        assertNotNull(result)
        assertEquals(OrderStatus.PAID.description, delivery.status)
        verify(exactly = 1) { orderRepository.save(any())  }
    }

    @ParameterizedTest
    @EnumSource(OrderStatus::class)
    fun `should throw exception when updating to an older status`(status: OrderStatus) {
        val delivery = DeliveryBuilder(status = OrderStatus.DELIVERED.description).build()
        val order = OrderBuilder(status = OrderStatus.DELIVERED.description).build()
        order.deliverys.add(delivery)
        delivery.order = order
        val expectedMessage = "Delivery with id '${delivery.id}' in Order with id ${order.id}" +
                " cannot be updated to status '${status.description}' " +
                "because it is already in status '${delivery.status}'."

        every { orderRepository.findById(order.id!!) } returns order

        val exception = assertThrows(IllegalArgumentException::class.java) {
            orderService.updateStatus(order.id!!, delivery.id, status)
        }

        assertEquals(exception.message,expectedMessage)
    }

    @Test
    fun `should create order successfully`() {
        val customer = CustomerBuilder(id = 1L).build()
        val address = customer.addresses.first()
        val contact = customer.contacts.first()
        val product = ProductBuilder(sku = "PROD1").build()

        val newOrderDTO =
            NewOrderDTO(
                customerId = customer.id,
                addressId = address.id,
                contactId = contact.id,
                items = listOf(
                    ItemInNewOrderDTO("PROD1", 2, BigDecimal.TEN)
                ),
                paymentMethods = listOf(
                    PaymentInNewOrder(
                        "CREDIT_CARD",
                        1,
                        BigDecimal.valueOf(20),
                        BigDecimal.ZERO)
                ),
            )

        every { customerRepository.findById(customer.id) } returns customer
        every { productRepository.findBySku("PROD1") } returns product
        every { orderRepository.save(any())} returns OrderBuilder().build()

        val result = orderService.createOrder(newOrderDTO)

        assertNotNull(result)
        verify(exactly = 1) {
            orderRepository.save(any())
            customerRepository.findById(customer.id)
        }
        verify(exactly = newOrderDTO.items!!.size) {
            productRepository.findBySku(any())
        }
    }
}
