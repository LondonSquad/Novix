@file:KoverIgnore

package com.london.data.local.database.dao

import com.london.domain.KoverIgnore

interface HomeDao<T> {
    suspend fun insert(item: T)
    suspend fun deleteAll()
    suspend fun getAll(): List<T>
    suspend fun insertAll(item: List<T>)
    suspend fun getCurrentPopularByDate(date: Long): T
}