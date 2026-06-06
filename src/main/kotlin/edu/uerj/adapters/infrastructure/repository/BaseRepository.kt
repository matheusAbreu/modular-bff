package edu.uerj.adapters.infrastructure.repository

import io.quarkus.hibernate.orm.PersistenceUnit
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject
import jakarta.persistence.EntityManager

@ApplicationScoped
class BaseRepository {
    @Inject
    @PersistenceUnit("bff")
    protected lateinit var entityManager: Instance<EntityManager>

    fun getEntityManager() =
        if (entityManager.isResolvable) {
            entityManager.get()
        } else {
            throw IllegalStateException("EntityManager is not resolvable")
        }
}
