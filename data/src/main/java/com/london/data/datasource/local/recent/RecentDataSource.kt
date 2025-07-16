package com.london.data.datasource.local.recent

interface RecentDataSource<T> {
    suspend fun insert(item: T)
    suspend fun clearOlderThanTen()
    suspend fun getAll(): List<T>
    suspend fun getRecentTen(): List<T>
    suspend fun insertAndKeepLastTen(item: T)
    suspend fun clearAll()
}