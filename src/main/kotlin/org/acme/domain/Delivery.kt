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
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "entregas")
class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    var order: Order? = null

//    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "OrderAddress")
//    var addressId: OrderAddress? = null

//    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "OrderItem")
//    var items: MutableSet<OrderItem> = mutableSetOf()
//
//    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "FiscalNote")
//    var fiscalNoteDanfe: FiscalNote? = null

    @Column(name = "telefone_contato")
    var contactPhone: String? = null

    @Column(name = "nome_contato")
    var contactName: String? = null

    @Column(name = "frete")
    var freight: Double = 0.0

    @Column(name = "status")
    var status: String? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null
}
