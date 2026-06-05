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
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "contatos")
class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    var customer: Customer? = null

    @Column(name = "principal")
    var primary: Boolean = false

    @Column(name = "tipo_contato")
    var contactType: String? = null

    @Column(name = "valor")
    var value: String? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(customer: Customer?, primary: Boolean, contactType: String?, value: String?) {
        this.customer = customer
        this.primary = primary
        this.contactType = contactType
        this.value = value
    }
}
