@file:KoverIgnore

package com.london.data.local.database.dao

import com.london.domain.KoverIgnore

interface SearchDao<T> {
    suspend fun insert(search: T)
    suspend fun update(search: T)
    suspend fun delete(search: T)
    suspend fun getAll(): List<T>
    suspend fun getCurrentSearchByDate(date: Long): T
    suspend fun getSearchByQuery(query: String): T
    suspend fun getSearchByQueryAndPage(query: String, page: Int): T
}