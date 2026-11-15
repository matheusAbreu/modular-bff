package edu.uerj.adapters.infrastructure.repository

import edu.uerj.config.ConfigRepository
import edu.uerj.domain.Customer
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import java.time.LocalDateTime

@ApplicationScoped
class CustomerRepository : ConfigRepository() {
    companion object {
        private const val SELECT_BY_ID = "SELECT c FROM Customer c WHERE c.id = :customerId"
    }

    fun findById(id: Long): Customer? =
        try {
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
