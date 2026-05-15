package org.acme.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "contatos")
class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "cliente_id")
    var clientId: Long = 0

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
