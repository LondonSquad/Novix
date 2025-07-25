@file:KoverIgnore

package com.london.data.remote.source.toprated.movie

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.toprated.movie.api.TopRatedMovieApiService
import com.london.data.datasource.remote.toprated.movie.model.TopRatedMovieRemote
import com.london.domain.KoverIgnore
import org.koin.core.annotation.Single

@Single
class TopRatedMovieRemoteDataSourceImpl(
    private val topRatedMovieApi: TopRatedMovieApiService
) : TopRatedMovieRemoteDataSource {
    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): ApiResponse<TopRatedMovieRemote> =
        topRatedMovieApi.getTopRatedMovies(pageNumber)
}
