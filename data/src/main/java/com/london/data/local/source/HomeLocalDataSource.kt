package com.london.data.local.source

interface HomeLocalDataSource<T> {
    suspend fun insert(item: T)
    suspend fun insertAll(items: List<T>)
    suspend fun deleteAll()
    suspend fun getAll(): List<T>
    suspend fun getByDate(date: Long): T
}
