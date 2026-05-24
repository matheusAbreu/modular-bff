package org.acme.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.enterprise.inject.Instance
import io.quarkus.hibernate.orm.PersistenceUnit

@ApplicationScoped
class BaseRepository {

    @Inject
    @PersistenceUnit("bff")
    protected lateinit var entityManager: Instance<EntityManager>


    fun getEntityManager() = if (entityManager.isResolvable)
            entityManager.get() else
        throw IllegalStateException("EntityManager is not resolvable")
}