package edu.uerj.domain

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
import java.math.BigDecimal

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

    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_id")
    var address: OrderAddress? = null

    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "nota_danfe")
    var fiscalNoteDanfe: FiscalNote? = null

    @Column(name = "telefone_contato")
    var contactPhone: String? = null

    @Column(name = "nome_contato")
    var contactName: String? = null

    @Column(name = "frete")
    var freight: BigDecimal = BigDecimal.ZERO

    @Column(name = "status")
    var status: String? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "delivery")
    val items: MutableList<OrderItem> = mutableListOf()
   
    constructor()

    constructor(
        order: Order?,
        address: OrderAddress?,
        contactPhone: String?,
        contactName: String?,
        freight: BigDecimal,
        status: String?
    ) {
        this.order = order
        this.address = address
        this.contactPhone = contactPhone
        this.contactName = contactName
        this.freight = freight
        this.status = status
    }
    
}
