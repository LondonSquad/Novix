package com.london.imageharamblur.ui

import android.util.Log
import androidx.collection.LruCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ModerationCacheManager {
    private const val CACHE_SIZE = 100

    private val cache = LruCache<String, ImageModerationState>(CACHE_SIZE)
    private val _cacheUpdates = MutableStateFlow(0)
    val cacheUpdates: StateFlow<Int> = _cacheUpdates

    fun get(key: String): ImageModerationState? = cache[key]

    fun put(key: String, state: ImageModerationState) {
        cache.put(key, state)
        _cacheUpdates.value++
    }

    fun clear() {
        cache.evictAll()
        _cacheUpdates.value++
    }

    fun size(): Int = cache.size()
}