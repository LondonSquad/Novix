package com.london.domain.repository

interface RecentRepository<T> {
    suspend fun insert(item: T)
    suspend fun getAll(): List<T>
    suspend fun clearAll()
    suspend fun delete(item: T)
}