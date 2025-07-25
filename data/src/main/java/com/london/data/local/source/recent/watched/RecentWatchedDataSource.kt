package com.london.data.local.source.recent.watched

interface RecentWatchedDataSource<T> {
    suspend fun getAll(): List<T>
    suspend fun insert(item: T)
}
