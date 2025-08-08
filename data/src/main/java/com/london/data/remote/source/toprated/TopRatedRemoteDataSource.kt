@file:KoverIgnore

package com.london.data.remote.source.toprated

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.data.remote.model.home.toprated.TopRatedTvSeriesRemote
import com.london.domain.KoverIgnore

interface TopRatedRemoteDataSource {

    suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedMovieRemote>>

    suspend fun getTopRatedTvShows(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedTvSeriesRemote>>
}