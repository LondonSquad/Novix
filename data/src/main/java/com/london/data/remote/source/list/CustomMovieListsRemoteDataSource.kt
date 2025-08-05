package com.london.data.remote.source.list

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.list.CreateCustomListResponse
import com.london.data.remote.model.list.CustomListResponse
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.model.search.MovieRemote

interface CustomMovieListsRemoteDataSource {

    suspend fun create(name: String, sessionId: String?, languageCode: String): Result<CreateCustomListResponse>
    suspend fun delete(listId: Int, sessionId: String?): Result<CustomListResponse>
    suspend fun getDetails(
        listId: Int,
        page: Int
    ): Result<ApiResponse<MovieRemote>>

    suspend fun addMovieToList(
        listId: Int,
        movieId: Int,
        sessionId: String?
    ): Result<CustomListResponse>

    suspend fun getAllMovieLists(
        page: Int,
        sessionId: String?
    ): Result<ApiResponse<CustomMovieListResponse>>

    suspend fun removeMovieFromList(
        listId: Int,
        movieId: Int,
        sessionId: String?
    ): Result<CustomListResponse>
}
