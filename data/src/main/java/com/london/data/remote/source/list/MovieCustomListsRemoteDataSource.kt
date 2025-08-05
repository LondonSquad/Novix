package com.london.data.remote.source.list

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.list.CreateCustomListResponse
import com.london.data.remote.model.list.CustomListResponse
import com.london.data.remote.model.search.MovieRemote

interface MovieCustomListsRemoteDataSource {

    suspend fun create(name: String): Result<CreateCustomListResponse>

    suspend fun delete(listId: Int): Result<CustomListResponse>

    suspend fun getDetails(listId: Int, page: Int): Result<ApiResponse<MovieRemote>>

    suspend fun addMovieToList(listId: Int, movieId: Int): Result<CustomListResponse>

    suspend fun removeMovieFromList(listId: Int, movieId: Int): Result<CustomListResponse>
}
