package com.london.data.datasource.local.dao.recent.whatched

interface RecentWatchedDao<T> {
    suspend fun getAll(): List<T>
    suspend fun insert(item: T)
}
