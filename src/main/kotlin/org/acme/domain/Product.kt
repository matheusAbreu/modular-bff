package org.acme.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
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
    var price: BigDecimal = BigDecimal.ZERO

    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, mappedBy = "product")
    val images: MutableList<ProductImage> = mutableListOf()
    constructor()

    constructor(sku: String, name: String?, description: String?, price: BigDecimal) {
        this.sku = sku
        this.name = name
        this.description = description
        this.price = price
    }

}
