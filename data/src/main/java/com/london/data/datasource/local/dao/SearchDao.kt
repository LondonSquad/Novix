package com.london.data.datasource.local.dao

interface SearchDao<T> {
    fun insert(search: T)
    fun update(search: T)
    fun delete(search: T)
    fun getAll(): T
    fun getCurrentSearch(date: Long): T
    fun getSearchByQuery(query: String): T
}