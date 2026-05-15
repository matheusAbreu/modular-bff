package org.acme.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "entregas")
class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "pedido_id")
    var orderId: Long = 0

    @Column(name = "endereco_id")
    var addressId: Long = 0

    @Column(name = "nota_danfe")
    var fiscalNoteDanfe: String? = null

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
