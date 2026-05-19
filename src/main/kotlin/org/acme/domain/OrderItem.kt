package org.acme.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "itens_pedido")
class OrderItem {

    @Id
    @Column(name = "id")
    var id: Long = 0

    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "productSku")
    var productSku: Product? = null

    @Column(name = "pedido_id")
    var orderId: Long = 0

    @Column(name = "entrega_id")
    var deliveryId: Long = 0

    @Column(name = "quantidade")
    var quantity: Int = 0

    @Column(name = "valor_unitario")
    var unitPrice: Double = 0.0

    @Column(name = "valor_total")
    var totalPrice: Double = 0.0

    @Column(name = "criado_em")
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null
}
