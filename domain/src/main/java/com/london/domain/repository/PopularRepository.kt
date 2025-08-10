package com.london.domain.repository

import com.london.domain.entity.popular.PopularMedia

interface PopularRepository {
    suspend fun getPopularMovies(): List<PopularMedia>
    suspend fun getPopularTvShows(): List<PopularMedia>
}
