package com.london.domain.repository

import com.london.domain.entity.popular.PopularMovie

interface PopularRepository {
    suspend fun getPopularMovies(): List<PopularMovie>
}