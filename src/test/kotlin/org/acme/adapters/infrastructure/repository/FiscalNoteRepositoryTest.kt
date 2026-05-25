package org.acme.adapters.infrastructure.repository

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.domain.FiscalNote
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import java.math.BigDecimal
import org.junit.jupiter.api.Test

@QuarkusTest
@Transactional
class FiscalNoteRepositoryTest {

    @Inject
    lateinit var fiscalNoteRepository: FiscalNoteRepository

    @Test
    fun `should save and find a fiscal note`() {
        val testId = (1..10000).random()
        val fiscalNote = FiscalNote(
            danfe = "DANFE-$testId",
            series = "SER-$testId",
            key = "KEY-$testId",
            file = "file-$testId.pdf",
            valueInNote = BigDecimal.valueOf(1500.00)
        )

        val savedFiscalNote = fiscalNoteRepository.save(fiscalNote)

        assertEquals(fiscalNote.danfe, savedFiscalNote.danfe)
        assertNotNull(savedFiscalNote.createdAt)

        val foundFiscalNote = fiscalNoteRepository.findByDanfe(savedFiscalNote.danfe!!)
        assertNotNull(foundFiscalNote)
        assertEquals(savedFiscalNote.series, foundFiscalNote?.series)
        assertEquals(savedFiscalNote.valueInNote, foundFiscalNote?.valueInNote)
    }

    @Test
    fun `should return null when fiscal note not found`() {
        val foundFiscalNote = fiscalNoteRepository.findByDanfe("NON-EXISTENT")
        assertNull(foundFiscalNote)
    }
}
