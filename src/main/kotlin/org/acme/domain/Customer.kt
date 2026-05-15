package org.acme.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "clientes")
class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "nome")
    var name: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "documento")
    var document: String? = null

    @Column(name = "tipo_documento")
    var documentType: String? = null

    @Column(name = "data_nascimento")
    var birthDate: LocalDateTime? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null
}
