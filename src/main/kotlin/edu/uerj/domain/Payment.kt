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
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "pagamentos")
class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    var order: Order? = null

    @Column(name = "metodo")
    var method: String? = null

    @Column(name = "quantidade_parcelas")
    var installments: Int = 0

    @Column(name = "valor_parcela")
    var installmentValue: BigDecimal = BigDecimal.ZERO

    @Column(name = "valor_total")
    var totalValue: BigDecimal = BigDecimal.ZERO

    @Column(name = "status")
    var status: String? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(
        order: Order?,
        method: String?,
        installments: Int,
        installmentValue: BigDecimal,
        totalValue: BigDecimal,
        status: String?,
    ) {
        this.order = order
        this.method = method
        this.installments = installments
        this.installmentValue = installmentValue
        this.totalValue = totalValue
        this.status = status
    }
}
