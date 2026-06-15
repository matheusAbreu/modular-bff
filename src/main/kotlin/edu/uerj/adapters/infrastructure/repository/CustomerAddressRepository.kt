package edu.uerj.adapters.infrastructure.repository

import edu.uerj.domain.CustomerAddress
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import java.time.LocalDateTime

@ApplicationScoped
class CustomerAddressRepository : BaseRepository() {
    companion object {
        private const val SELECT_BY_ID = "SELECT a FROM CustomerAddress a WHERE a.id = :addressId"
    }

    fun findById(id: Long): CustomerAddress? =
        try {
            getEntityManager()
                .createQuery(SELECT_BY_ID)
                .setParameter("addressId", id)
                .singleResult as CustomerAddress
        } catch (e: NoResultException) {
            null
        }

    fun save(address: CustomerAddress): CustomerAddress {
        address.updatedAt = LocalDateTime.now()
        val savedAddress = getEntityManager().merge(address)
        getEntityManager().flush()
        return savedAddress
    }
}
