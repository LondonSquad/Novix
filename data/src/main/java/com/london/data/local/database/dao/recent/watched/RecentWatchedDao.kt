@file:KoverIgnore

package com.london.data.local.database.dao.recent.watched

import com.london.domain.KoverIgnore
import kotlinx.coroutines.flow.Flow

interface RecentWatchedDao<T> {
    fun getAll(): Flow<List<T>>
    suspend fun insert(item: T)
}
