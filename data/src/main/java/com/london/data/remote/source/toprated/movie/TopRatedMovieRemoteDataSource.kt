@file:KoverIgnore

package com.london.data.remote.source.toprated.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.domain.KoverIgnore

interface TopRatedMovieRemoteDataSource {
    suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedMovieRemote>>
}