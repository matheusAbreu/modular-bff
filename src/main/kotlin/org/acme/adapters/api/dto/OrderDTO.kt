package org.acme.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.inject.Singleton
import org.acme.domain.Order
import java.math.BigDecimal
import java.time.LocalDateTime


data class OrderDTO(
    val id: Long? = null,
    val status: String? = null,
    val total: BigDecimal = BigDecimal.ZERO,
    val discount: BigDecimal = BigDecimal.ZERO,
    val freight: BigDecimal = BigDecimal.ZERO,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(order: Order) : this(
        id = order.id,
        status = order.status,
        total = order.total,
        discount = order.discount,
        freight = order.freight,
        createdAt = order.createdAt,
        updatedAt = order.updatedAt
    )
}