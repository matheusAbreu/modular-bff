package org.acme.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "pedidos")
class Order {

    @Id
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    var client: Customer? = null

    @Column(name = "status")
    var status: String? = null 

    @Column(name = "total")
    var total: Double = 0.0

    @Column(name = "desconto")
    var discount: Double = 0.0

    @Column(name = "frete")
    var freight: Double = 0.0

    @Column(name = "criado_em")
    var createdAt: java.time.LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: java.time.LocalDateTime? = null

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "order")
    var deliverys: MutableSet<Delivery> = HashSet()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "order")
    var payments: MutableSet<Payment> = HashSet()
}