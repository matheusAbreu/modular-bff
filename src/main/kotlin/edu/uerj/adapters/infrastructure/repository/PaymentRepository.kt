package edu.uerj.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import edu.uerj.domain.Payment
import java.time.LocalDateTime

@ApplicationScoped
class PaymentRepository: BaseRepository() {

    companion object {
        private const val SELECT_BY_ID = "SELECT p FROM Payment p WHERE p.id = :paymentId"
    }

    fun findById(id: Long): Payment? = try {
        getEntityManager()
            .createQuery(SELECT_BY_ID)
            .setParameter("paymentId", id)
            .singleResult as Payment
    } catch (e: NoResultException) {
        null
    }

    fun save(payment: Payment): Payment {
        payment.updatedAt = java.time.LocalDateTime.now()
        val savedPayment = getEntityManager().merge(payment)
        getEntityManager().flush()
        return savedPayment
    }
}
