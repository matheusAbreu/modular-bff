package edu.uerj.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "pedidos")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    var customer: Customer? = null

    @Column(name = "status")
    var status: String? = null

    @Column(name = "total")
    var total: BigDecimal = BigDecimal.ZERO

    @Column(name = "desconto")
    var discount: BigDecimal = BigDecimal.ZERO

    @Column(name = "frete")
    var freight: BigDecimal = BigDecimal.ZERO

    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "order")
    val deliverys: MutableList<Delivery> = mutableListOf()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "order")
    val payments: MutableList<Payment> = mutableListOf()

    constructor()

    constructor(status: String, total: BigDecimal, discount: BigDecimal, freight: BigDecimal, customer: Customer) {
        this.status = status
        this.total = total
        this.discount = discount
        this.freight = freight
        this.customer = customer
    }
}
