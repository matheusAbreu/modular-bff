package org.acme.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import org.acme.domain.Customer
import java.time.LocalDateTime

@ApplicationScoped
class CustomerRepository: BaseRepository() {

    companion object {
        private const val SELECT_BY_ID = "SELECT c FROM Customer c WHERE c.id = :customerId"
    }

    fun findById(id: Long): Customer? = try {
        getEntityManager()
            .createQuery(SELECT_BY_ID)
            .setParameter("customerId", id)
            .singleResult as Customer
    } catch (e: NoResultException) {
        null
    }

    fun save(customer: Customer): Customer {
        customer.updatedAt = LocalDateTime.now()
        val savedCustomer = getEntityManager().merge(customer)
        getEntityManager().flush()
        return savedCustomer
    }
}
