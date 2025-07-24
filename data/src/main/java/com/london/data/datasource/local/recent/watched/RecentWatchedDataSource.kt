package com.london.data.datasource.local.recent.watched

interface RecentWatchedDataSource<T> {
    suspend fun getAll(): List<T>
    suspend fun insert(item: T)
}
