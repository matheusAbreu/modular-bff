package org.acme.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "contatos")
class Contact {

    @Id
    @Column(name = "id")
    var id: Long = 0

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    var client: Customer? = null

    @Column(name = "principal")
    var primary: Boolean = false

    @Column(name = "tipo_contato")
    var contactType: String? = null

    @Column(name = "valor")
    var value: String? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null
}
