package org.acme.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.enterprise.inject.Instance
import jakarta.persistence.PersistenceUnit

@ApplicationScoped
class BaseRepository {

    @Inject
    @PersistenceUnit(name = "bff")
    protected lateinit var entityManager: Instance<EntityManager>
    
    fun getEntityManager() = entityManager.get()
}