package com.london.data.local.source

interface HomeLocalDataSource<T> {
    suspend fun insert(item: T)
    suspend fun insertAll(items: List<T>)
    suspend fun deleteAll()
    suspend fun getAll(): List<T>
    suspend fun getCurrentPopularByDate(date: Long): T

    fun Long.isDayExpired(): Boolean {
        val oneDayInMillis = 24 * 60 * 60 * 1000L
        val oneDayAgo = System.currentTimeMillis() - oneDayInMillis
        return System.currentTimeMillis() < oneDayAgo
    }
}
