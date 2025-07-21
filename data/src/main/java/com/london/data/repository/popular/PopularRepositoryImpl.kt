package com.london.data.repository.popular

import com.london.data.datasource.remote.home.popular.PopularRemoteDataSource
import com.london.data.mapper.popular.toPopularMovies
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.repository.PopularRepository

class PopularRepositoryImpl(
    private val popularMoviesRemoteDataSource: PopularRemoteDataSource
) : PopularRepository {
    override suspend fun getPopularMovies(): List<PopularMovie> {
        return popularMoviesRemoteDataSource.getPopularMovies().toPopularMovies()
    }
}