package edu.uerj.adapters.api.dto

class PaginatedResult<T>(
    var items: MutableList<T>? = null,
    var limit: Int? = null,
    var offset: Int? = null,
    var total: Int? = null,
)
