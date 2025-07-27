@file:KoverIgnore

package com.london.data.remote.source.toprated.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.movie.model.TopRatedMovieRemote
import com.london.data.remote.service.toprated.movie.TopRatedMovieApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.domain.KoverIgnore
import org.koin.core.annotation.Single

@Single
class TopRatedMovieRemoteDataSourceImpl(
    private val topRatedMovieApi: TopRatedMovieApiService
) : TopRatedMovieRemoteDataSource , BaseRemoteDatasource {
    override suspend fun getTopRatedMovies(
        pageNumber: Int,
        language: String,
        region: String
    ): Result<ApiResponse<TopRatedMovieRemote>> =
        callApiWithRetry(
            apiCall = { topRatedMovieApi.getTopRatedMovies(pageNumber, language, region) },
            mapper = { it }
        )
}
