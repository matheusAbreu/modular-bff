package org.acme.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "produtos")
class Product {

    @Id
    @Column(name = "sku")
    var sku: String = ""

    @Column(name = "nome")
    var name: String? = null

    @Column(name = "descricao")
    var description: String? = null

    @Column(name = "preco")
    var price: Double = 0.0

    @Column(name = "criado_em")
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "product")
    var images: MutableSet<ProductImage> = mutableSetOf()
}
