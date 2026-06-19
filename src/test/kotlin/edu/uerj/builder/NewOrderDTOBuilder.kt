package edu.uerj.builder

import edu.uerj.adapters.api.dto.item.ItemInNewOrderDTO
import edu.uerj.adapters.api.dto.order.NewOrderDTO
import edu.uerj.adapters.api.dto.payment.PaymentInNewOrderDTO

class NewOrderDTOBuilder(
    var customerId: Long = (1..100).random().toLong(),
    var addressId: Long = (1..100).random().toLong(),
    var contactId: Long = (1..100).random().toLong(),
    var items: MutableList<ItemInNewOrderDTO> =
        mutableListOf(
            ItemInNewOrderDTOBuilder().build(),
        ),
    var paymentMethods: MutableList<PaymentInNewOrderDTO> =
        mutableListOf(
            PaymentInNewOrderDTOBuilder().build(),
        ),
) {
    fun withItems(vararg items: ItemInNewOrderDTO) =
        apply {
            this.items = items.toMutableList()
        }

    fun withPaymentMethods(vararg paymentMethods: PaymentInNewOrderDTO) =
        apply {
            this.paymentMethods = paymentMethods.toMutableList()
        }

    fun build() =
        NewOrderDTO(
            customerId = customerId,
            addressId = addressId,
            contactId = contactId,
            items = items,
            paymentMethods = paymentMethods,
        )
}
