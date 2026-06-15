package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import edu.uerj.domain.FiscalNote
import java.math.BigDecimal
import java.time.LocalDateTime

data class FiscalNoteDTO(
    var danfe: String? = null,
    var series: String? = null,
    var key: String? = null,
    var file: String? = null,
    var valueInNote: BigDecimal = BigDecimal.ZERO,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updatedAt: LocalDateTime? = null,
) {
    constructor(fiscalNote: FiscalNote) : this() {
        this.danfe = fiscalNote.danfe
        this.series = fiscalNote.series
        this.key = fiscalNote.key
        this.file = fiscalNote.file
        this.valueInNote = fiscalNote.valueInNote
        this.createdAt = fiscalNote.createdAt
        this.updatedAt = fiscalNote.updatedAt
    }
}
