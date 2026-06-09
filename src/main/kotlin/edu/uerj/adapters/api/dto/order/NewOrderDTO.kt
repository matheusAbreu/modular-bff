package edu.uerj.adapters.api.dto.order

import edu.uerj.adapters.api.dto.item.ItemInNewOrderDTO
import edu.uerj.adapters.api.dto.payment.PaymentInNewOrder

data class NewOrderDTO(
    var customerId: Long = 0,
    var addressId: Long = 0,
    var contactId: Long = 0,
    var items: List<ItemInNewOrderDTO> = listOf(),
    var paymentMethods: List<PaymentInNewOrder> = listOf(),
)
