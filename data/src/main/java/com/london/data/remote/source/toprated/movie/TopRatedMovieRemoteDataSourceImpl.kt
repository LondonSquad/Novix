@file:KoverIgnore

package com.london.data.remote.source.toprated.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.TopRatedMovieRemote
import com.london.data.remote.service.toprated.TopRatedMovieApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.domain.KoverIgnore
import javax.inject.Inject

class TopRatedMovieRemoteDataSourceImpl @Inject constructor(
    private val topRatedMovieApi: TopRatedMovieApiService
) : TopRatedMovieRemoteDataSource , BaseRemoteDatasource {
    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedMovieRemote>> =
        callApiWithRetry(
            apiCall = { topRatedMovieApi.getTopRatedMovies(pageNumber) },
            mapper = { it }
        )
}