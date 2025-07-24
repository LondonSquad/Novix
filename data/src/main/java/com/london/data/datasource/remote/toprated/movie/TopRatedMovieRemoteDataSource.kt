@file:KoverIgnore

package com.london.data.datasource.remote.toprated.movie

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.toprated.movie.model.TopRatedMovieRemote
import com.london.domain.KoverIgnore

interface TopRatedMovieRemoteDataSource {
    suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): ApiResponse<TopRatedMovieRemote>
}