package edu.uerj.adapters.api.dto

import com.fasterxml.jackson.annotation.*

class PaginatedResult<T>(
    var items: MutableList<T>? = null,
    var limit: Long? = null,
    var total: Long? = null,
){
    @JsonProperty("quantityInPage")
    fun quantityInPage() = items?.count() ?: 0
}
