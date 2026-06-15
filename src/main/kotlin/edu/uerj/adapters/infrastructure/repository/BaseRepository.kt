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
    private lateinit var entityManager: Instance<EntityManager>

    fun getEntityManager(): EntityManager {
        if (entityManager.isResolvable) {
            return entityManager.get()
        }
        error("EntityManager is not resolvable")
    }
}
