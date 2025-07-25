package com.london.data.repository

import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSource
import com.london.data.mapper.toprated.toEntity
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.TopRatedMovieRepository
import org.koin.core.annotation.Single

@Single
class TopRatedMovieRepoImpl(
    private val topRatedMovieRemoteDataSource: TopRatedMovieRemoteDataSource
) : TopRatedMovieRepository {
    override suspend fun getTopRatedMovies(
        pageNumber: Int,
        language: String,
        region: String
    ): List<TopRatedMovie> =
        topRatedMovieRemoteDataSource.getTopRatedMovies(
            pageNumber,
            language,
            region
        ).items.map { it.toEntity() }
}
