package edu.uerj.adapters.infrastructure.repository

import edu.uerj.domain.Contact
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import java.time.LocalDateTime

@ApplicationScoped
class ContactRepository : BaseRepository() {
    companion object {
        private const val SELECT_BY_ID = "SELECT c FROM Contact c WHERE c.id = :contactId"
    }

    fun findById(id: Long): Contact? =
        try {
            getEntityManager()
                .createQuery(SELECT_BY_ID)
                .setParameter("contactId", id)
                .singleResult as Contact
        } catch (e: NoResultException) {
            null
        }

    fun save(contact: Contact): Contact {
        contact.updatedAt = LocalDateTime.now()
        val savedContact = getEntityManager().merge(contact)
        getEntityManager().flush()
        return savedContact
    }
}
