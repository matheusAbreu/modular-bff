package edu.uerj.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.math.BigDecimal

@Entity
@Table(name = "notas_fiscais")
class FiscalNote {

    @Id
    @Column(name = "danfe")
    var danfe: String? = null

    @Column(name = "serie")
    var series: String? = null

    @Column(name = "chave")
    var key: String? = null

    @Column(name = "arquivo")
    var file: String? = null

    @Column(name = "valor_em_nota")
    var valueInNote: BigDecimal = BigDecimal.ZERO
    
    @Column(name = "criado_em")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "atualizado_em")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(danfe: String?, series: String?, key: String?, file: String?, valueInNote: BigDecimal) {
        this.danfe = danfe
        this.series = series
        this.key = key
        this.file = file
        this.valueInNote = valueInNote
    }
}
