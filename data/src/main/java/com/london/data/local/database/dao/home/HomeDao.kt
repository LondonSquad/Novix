@file:KoverIgnore

package com.london.data.local.database.dao.home

import com.london.domain.KoverIgnore

interface HomeDao<T> {
    suspend fun insert(item: T)
    suspend fun deleteAll()
    suspend fun getAll(): List<T>
    suspend fun insertAll(items: List<T>)
    suspend fun getByDate(date: Long): T
}