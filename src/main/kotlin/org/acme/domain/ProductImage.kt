package org.acme.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "imagens_produtos")
class ProductImage {

    @Id
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "produto_sku")
    var productSku: String? = null

    @Column(name = "base_64")
    var base64: String? = null

    @Column(name = "criado_em")
    var createdAt: LocalDateTime? = null

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime? = null
}
