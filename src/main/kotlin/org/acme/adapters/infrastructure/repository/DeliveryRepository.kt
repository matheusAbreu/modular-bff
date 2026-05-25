package org.acme.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import org.acme.domain.Delivery
import java.time.LocalDateTime


@ApplicationScoped
class DeliveryRepository: BaseRepository() {

    companion object {
        private const val SELECT_BY_ID = "SELECT d FROM Delivery d WHERE d.id = :deliveryId"
    }

    fun findById(id: Long): Delivery? = try {
        getEntityManager()
            .createQuery(SELECT_BY_ID)
            .setParameter("deliveryId", id)
            .singleResult as Delivery
    } catch (e: NoResultException) {
        null
    }

    fun save(delivery: Delivery): Delivery {
        delivery.updatedAt = LocalDateTime.now()
        val savedDelivery = getEntityManager().merge(delivery)
        getEntityManager().flush()
        return savedDelivery
    }
}
