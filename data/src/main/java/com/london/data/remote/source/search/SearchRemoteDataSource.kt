@file:KoverIgnore

package com.london.data.remote.source.search

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.MovieRemote
import com.london.data.remote.model.search.model.SearchTvShowRemote
import com.london.data.remote.model.search.model.searchactormodel.SearchActorRemote
import com.london.domain.KoverIgnore

interface SearchRemoteDataSource {

    suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<MovieRemote>>

    suspend fun searchForTvShows(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<SearchTvShowRemote>>

    suspend fun searchForActors(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<SearchActorRemote>>

}
