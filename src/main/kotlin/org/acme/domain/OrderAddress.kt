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
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "enderecos_pedidos")
class OrderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    var customer: Customer? = null

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    var order: Order? = null

    @Column(name = "cep")
    var zipCode: String? = null

    @Column(name = "logradouro")
    var street: String? = null

    @Column(name = "numero")
    var number: String? = null

    @Column(name = "complemento")
    var complement: String? = null

    @Column(name = "cidade")
    var city: String? = null

    @Column(name = "estado")
    var state: String? = null
    
    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        customer: Customer?,
        order: Order?,
        zipCode: String?,
        street: String?,
        number: String?,
        complement: String?,
        city: String?,
        state: String?
    ) {
        this.customer = customer
        this.order = order
        this.zipCode = zipCode
        this.street = street
        this.number = number
        this.complement = complement
        this.city = city
        this.state = state
    }
}
