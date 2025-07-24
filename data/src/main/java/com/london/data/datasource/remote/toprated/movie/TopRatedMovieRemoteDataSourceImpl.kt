@file:KoverIgnore

package com.london.data.datasource.remote.toprated.movie

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.toprated.movie.api.TopRatedMovieApi
import com.london.data.datasource.remote.toprated.movie.model.TopRatedMovieRemote
import com.london.domain.KoverIgnore
import org.koin.core.annotation.Single

@Single
class TopRatedMovieRemoteDataSourceImpl(
    private val topRatedMovieApi: TopRatedMovieApi
) : TopRatedMovieRemoteDataSource {
    override suspend fun getTopRatedMovies(
        pageNumber: Int,
        language: String,
        region: String
    ): ApiResponse<TopRatedMovieRemote> =
        topRatedMovieApi.getTopRatedMovies(pageNumber, language, region)
}
