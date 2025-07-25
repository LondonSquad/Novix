@file:KoverIgnore

package com.london.data.local.database.dao.recent.whatched

import com.london.domain.KoverIgnore

interface RecentWatchedDao<T> {
    suspend fun getAll(): List<T>
    suspend fun insert(item: T)
}
