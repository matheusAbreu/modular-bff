package org.acme.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "pagamentos")
class Payment {

    @Id
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "pedido_id")
    var orderId: Long = 0

    @Column(name = "metodo")
    var method: String? = null

    @Column(name = "quantidade_parcelas")
    var installments: Int = 0

    @Column(name = "valor_parcela")
    var installmentValue: Double = 0.0

    @Column(name = "valor_total")
    var totalValue: Double = 0.0

    @Column(name = "status")
    var status: String? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null
}
