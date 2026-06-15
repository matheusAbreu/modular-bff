package edu.uerj.domain.enums

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@QuarkusTest
class OrderStatusTest {
    @ParameterizedTest
    @MethodSource("getAllIds")
    fun `should return correct status by id`(
        id: Long,
        expectedStatus: OrderStatus,
    ) {
        val result = OrderStatus.getById(id)
        assertEquals(expectedStatus, result)
    }

    @Test
    fun `should throw exception when id does not exist`() {
        val invalidId = -1L
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                OrderStatus.getById(invalidId)
            }
        assertEquals("No OrderStatus enumeration constant with a id '$invalidId'", exception.message)
    }

    @ParameterizedTest
    @MethodSource("getAllDescriptions")
    fun `should return correct status by description`(
        expectedStatus: OrderStatus,
        description: String,
    ) {
        val result = OrderStatus.getByDescription(description)
        assertEquals(expectedStatus, result)
    }

    @Test
    fun `should return correct status by description regardless of case`() {
        val status = OrderStatus.PAID
        val result = OrderStatus.getByDescription(status.description.lowercase())
        assertEquals(status, result)

        val resultUpper = OrderStatus.getByDescription(status.description.uppercase())
        assertEquals(status, resultUpper)
    }

    @Test
    fun `should throw exception when description does not exist`() {
        val invalidDescription = "Invalid Status"
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                OrderStatus.getByDescription(invalidDescription)
            }
        assertEquals("No OrderStatus enumeration constant with a description '$invalidDescription'", exception.message)
    }

    companion object {
        @JvmStatic
        fun getAllIds(): Stream<Arguments> =
            Stream.of(
                Arguments.of(0, OrderStatus.NEW_ORDER),
                Arguments.of(1, OrderStatus.WAITING_PAYMENT),
                Arguments.of(2, OrderStatus.UNDER_FRAUD_ANALYSIS),
                Arguments.of(3, OrderStatus.PAID),
                Arguments.of(4, OrderStatus.INVOICE_BEGINNING),
                Arguments.of(5, OrderStatus.INVOICED),
                Arguments.of(6, OrderStatus.SEPARATION_BEGINNING),
                Arguments.of(7, OrderStatus.SEPARATION_FINISHED),
                Arguments.of(8, OrderStatus.DELIVERED),
            )

        @JvmStatic
        fun getAllDescriptions(): Stream<Arguments> =
            Stream.of(
                Arguments.of(OrderStatus.NEW_ORDER, "Novo Pedido"),
                Arguments.of(OrderStatus.WAITING_PAYMENT, "Aguardando Pagamento"),
                Arguments.of(OrderStatus.UNDER_FRAUD_ANALYSIS, "Em análise de fraude"),
                Arguments.of(OrderStatus.PAID, "Pagamento efetuado"),
                Arguments.of(OrderStatus.INVOICE_BEGINNING, "Faturamento iniciado"),
                Arguments.of(OrderStatus.INVOICED, "Faturamento realizado"),
                Arguments.of(OrderStatus.SEPARATION_BEGINNING, "Separação iniciada"),
                Arguments.of(OrderStatus.SEPARATION_FINISHED, "Separação realizada"),
                Arguments.of(OrderStatus.DELIVERED, "Produto Entregue"),
            )
    }
}
