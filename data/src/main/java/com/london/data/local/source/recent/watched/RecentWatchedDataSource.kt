package com.london.data.local.source.recent.watched

import kotlinx.coroutines.flow.Flow

interface RecentWatchedDataSource<T> {
    suspend fun getAll(): Flow<List<T>>
    suspend fun insert(item: T)
}
