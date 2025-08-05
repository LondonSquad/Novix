package com.london.data.remote.source.list

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.list.CreateCustomListBody
import com.london.data.remote.model.list.CreateCustomListResponse
import com.london.data.remote.model.list.CustomListResponse
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.model.list.ListMovieBody
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.service.list.CustomMovieListsApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class CustomMovieListsRemoteDataSourceImpl @Inject constructor(
    val customMovieListsApiService: CustomMovieListsApiService
) : CustomMovieListsRemoteDataSource, BaseRemoteDatasource {

    override suspend fun create(
        name: String,
        sessionId: String?,
        languageCode: String
    ): Result<CreateCustomListResponse> = callApiWithRetry(
        apiCall = {
            customMovieListsApiService.create(
                sessionId = sessionId,
                createCustomListBody = CreateCustomListBody(
                    name = name,
                    description = "",
                    languageCode = languageCode,
                )
            )
        },
        mapper = { it }
    )

    override suspend fun getDetails(
        listId: Int,
        page: Int
    ): Result<ApiResponse<MovieRemote>> = callApiWithRetry(
        apiCall = {
            customMovieListsApiService.getDetails(
                listId = listId,
                page = page
            )
        },
        mapper = { it }
    )

    override suspend fun delete(
        listId: Int,
        sessionId: String?
    ): Result<CustomListResponse> = callApiWithRetry(
        apiCall = {
            customMovieListsApiService.delete(
                listId = listId,
                sessionId = sessionId
            )
        },
        mapper = { it }
    )

    override suspend fun addMovieToList(
        listId: Int,
        movieId: Int,
        sessionId: String?
    ): Result<CustomListResponse> = callApiWithRetry(
        apiCall = {
            customMovieListsApiService.addMovieToList(
                listId = listId,
                sessionId = sessionId,
                movieAdditionBody = ListMovieBody(mediaId = movieId)
            )
        },
        mapper = { it }
    )

    override suspend fun removeMovieFromList(
        listId: Int,
        movieId: Int,
        sessionId: String?
    ): Result<CustomListResponse> = callApiWithRetry(
        apiCall = {
            customMovieListsApiService.removeMovieFromList(
                listId = listId,
                sessionId = sessionId,
                movieDeletionBody = ListMovieBody(mediaId = movieId)
            )
        },
        mapper = { it }
    )

    override suspend fun getAllMovieLists(
        page: Int,
        sessionId: String?
    ): Result<ApiResponse<CustomMovieListResponse>> = callApiWithRetry(
        apiCall = {
            customMovieListsApiService.getAllUserLists(
                sessionId = sessionId
            )
        },
        mapper = { it }
    )

}
