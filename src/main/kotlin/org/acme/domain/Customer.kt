package org.acme.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.OneToMany
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
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "customer")
    var contacts: MutableSet<Contact> = mutableSetOf()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "customer")
    var addresses: MutableSet<CustomerAddress> = mutableSetOf()


    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "customer")
    fun getOrders():MutableSet<Order>{
        val orders: MutableSet<Order> = mutableSetOf()

        return orders
    }

    constructor()

    constructor(name: String, document: String, documentType: String, email: String, birthDate: LocalDateTime) {
        this.name = name
        this.document = document
        this.email = email
        this.birthDate = birthDate
        this.documentType = documentType
    }
}
