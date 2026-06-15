package edu.uerj.adapters.infrastructure.repository

import edu.uerj.domain.OrderAddress
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import java.time.LocalDateTime

@ApplicationScoped
class OrderAddressRepository : BaseRepository() {
    companion object {
        private const val SELECT_BY_ID = "SELECT a FROM OrderAddress a WHERE a.id = :addressId"
    }

    fun findById(id: Long): OrderAddress? =
        try {
            getEntityManager()
                .createQuery(SELECT_BY_ID)
                .setParameter("addressId", id)
                .singleResult as OrderAddress
        } catch (e: NoResultException) {
            null
        }

    fun save(address: OrderAddress): OrderAddress {
        address.updatedAt = LocalDateTime.now()
        val savedAddress = getEntityManager().merge(address)
        getEntityManager().flush()
        return savedAddress
    }
}
