package edu.uerj.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import edu.uerj.domain.FiscalNote
import java.time.LocalDateTime


@ApplicationScoped
class FiscalNoteRepository: BaseRepository() {

    companion object {
        private const val SELECT_BY_DANFE = "SELECT f FROM FiscalNote f WHERE f.danfe = :danfe"
    }

    fun findByDanfe(danfe: String): FiscalNote? = try {
        getEntityManager()
            .createQuery(SELECT_BY_DANFE)
            .setParameter("danfe", danfe)
            .singleResult as FiscalNote
    } catch (e: NoResultException) {
        null
    }

    fun save(fiscalNote: FiscalNote): FiscalNote {
        fiscalNote.updatedAt = LocalDateTime.now()
        val savedFiscalNote = getEntityManager().merge(fiscalNote)
        getEntityManager().flush()
        return savedFiscalNote
    }
}
