package com.london.data.local.database.dao.home

interface HomeDao<T> {
    suspend fun deleteAll()
    suspend fun insert(item: T)
    suspend fun getAll(): List<T>
    suspend fun getByDate(date: Long): T
    suspend fun insertAll(items: List<T>)
}
