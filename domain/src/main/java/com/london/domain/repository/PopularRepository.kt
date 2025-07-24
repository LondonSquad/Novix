package com.london.domain.repository

import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.popular.PopularTvShow

interface PopularRepository {
    suspend fun getPopularMovies(): List<PopularMovie>
    suspend fun getPopularTvShows(): List<PopularTvShow>
}