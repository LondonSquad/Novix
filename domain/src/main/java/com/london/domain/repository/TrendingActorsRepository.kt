package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse

interface TrendingActorsRepository {
    suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor>
} 