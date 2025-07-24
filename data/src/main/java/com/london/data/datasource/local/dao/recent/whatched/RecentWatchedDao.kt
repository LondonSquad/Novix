@file:KoverIgnore
package com.london.data.datasource.local.dao.recent.whatched

import com.london.domain.KoverIgnore

interface RecentWatchedDao<T> {
    suspend fun getAll(): List<T>
    suspend fun insert(item: T)
}
