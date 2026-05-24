package org.acme.adapters.infrastructure.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import org.acme.domain.Order

@ApplicationScoped
class OrderRepository: BaseRepository() {


    fun findById(id: Long): Order? {
        try {
            return getEntityManager().find(Order::class.java, id)
        }
        catch (exception: NoResultException) {
            return null
        } 
        catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    fun save(order: Order): Order {
        getEntityManager().transaction.begin()
        val savedOrder = getEntityManager().merge(order)
        getEntityManager().transaction.commit()
        return savedOrder
    }
}