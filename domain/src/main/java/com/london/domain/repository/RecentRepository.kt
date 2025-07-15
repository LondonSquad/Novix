package com.london.domain.repository

interface RecentRepository {
    suspend fun insert(item: String)
    suspend fun getAll(): List<String>
    suspend fun clearAll()
}