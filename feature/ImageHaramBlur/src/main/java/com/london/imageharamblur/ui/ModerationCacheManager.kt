package com.london.imageharamblur.ui

import androidx.collection.LruCache
import kotlinx.coroutines.flow.MutableStateFlow

object ModerationCacheManager {
    private const val CACHE_SIZE = 100

    private val cache = LruCache<String, ImageModerationState>(CACHE_SIZE)
    private val _cacheUpdates = MutableStateFlow(0)

    fun get(key: String): ImageModerationState? = cache[key]


    fun put(key: String, state: ImageModerationState) {
        cache.put(key, state)
        _cacheUpdates.value++
    }
}