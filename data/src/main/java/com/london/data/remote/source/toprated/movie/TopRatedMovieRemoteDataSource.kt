@file:KoverIgnore

package com.london.data.remote.source.toprated.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.movie.model.TopRatedMovieRemote
import com.london.domain.KoverIgnore
import retrofit2.Response

interface TopRatedMovieRemoteDataSource {
    suspend fun getTopRatedMovies(
        pageNumber: Int,
        language: String,
        region: String
    ): Result<ApiResponse<TopRatedMovieRemote>>
}