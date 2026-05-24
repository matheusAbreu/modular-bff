package org.acme.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "enderecos_pedidos")
class OrderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "cliente_id")
    var clientId: Long = 0

    @Column(name = "pedido_id")
    var orderId: Long = 0

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
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null
}
