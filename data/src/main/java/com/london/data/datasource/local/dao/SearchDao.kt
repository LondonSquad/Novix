package com.london.data.datasource.local.dao

interface SearchDao<T> {
    suspend fun insert(search: T)
    suspend fun update(search: T)
    suspend fun delete(search: T)
    suspend fun getAll(): T
    suspend fun getCurrentSearch(date: Long): T
    suspend fun getSearchByQuery(query: String): T
}