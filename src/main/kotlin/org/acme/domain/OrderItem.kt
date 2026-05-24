package org.acme.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "itens_pedido")
class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_sku")
    var product: Product? = null

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    var order: Order? = null

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "entrega_id")
    var delivery: Delivery? = null

    @Column(name = "quantidade")
    var quantity: Int = 0

    @Column(name = "valor_unitario")
    var unitPrice: Double = 0.0

    @Column(name = "valor_total")
    var totalPrice: Double = 0.0
    
    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        product: Product?,
        order: Order?,
        delivery: Delivery?,
        quantity: Int,
        unitPrice: Double,
        totalPrice: Double
    ) {
        this.product = product
        this.order = order
        this.delivery = delivery
        this.quantity = quantity
        this.unitPrice = unitPrice
        this.totalPrice = totalPrice
    }
}
