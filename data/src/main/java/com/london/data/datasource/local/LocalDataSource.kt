package com.london.data.datasource.local

import java.security.MessageDigest

interface LocalDataSource<T> {
    suspend fun insert(item: T)
    suspend fun update(item: T)
    suspend fun delete(item: T)
    suspend fun get(): List<T>
    suspend fun getByDate(date: Long): T
    suspend fun getByQuery(query: String): T

    fun String.generateHash(): String = MessageDigest.getInstance("MD5").digest(toByteArray())
        .joinToString("") { "%02x".format(it) }

    fun isOneHourExpired(date: Long): Boolean {
        val oneHourInMillis = 60 * 60 * 1000
        val oneHourAgo = System.currentTimeMillis() - oneHourInMillis
        return date < oneHourAgo
    }
}