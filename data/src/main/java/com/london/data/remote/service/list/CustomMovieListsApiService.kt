package com.london.data.remote.service.list

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.list.CreateCustomListBody
import com.london.data.remote.model.list.CreateCustomListResponse
import com.london.data.remote.model.list.CustomListResponse
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.model.list.ListDetailsResponse
import com.london.data.remote.model.list.ListMovieBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomMovieListsApiService {

    @POST(ApiConstants.CREATE_CUSTOM_LIST_PATH)
    suspend fun create(
        @Query("session_id") sessionId: String?,
        @Body createCustomListBody: CreateCustomListBody
    ): Response<CreateCustomListResponse>

    @DELETE(ApiConstants.DELETE_CUSTOM_LIST_PATH)
    suspend fun delete(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?
    ): Response<CustomListResponse>

    @GET(ApiConstants.GET_LIST_DETAILS_PATH)
    suspend fun getDetails(
        @Path("list_id") listId: Int,
        @Query("page") page: Int
    ): Response<ListDetailsResponse>

    @POST(ApiConstants.ADD_MOVIE_TO_LIST_PATH)
    suspend fun addMovieToList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?,
        @Body movieAdditionBody: ListMovieBody
    ): Response<CustomListResponse>

    @POST(ApiConstants.REMOVE_MOVIE_FROM_LIST_PATH)
    suspend fun removeMovieFromList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?,
        @Body movieDeletionBody: ListMovieBody
    ): Response<CustomListResponse>

    @GET(ApiConstants.GET_ACCOUNT_LISTS_PATH)
    suspend fun getAllUserLists(
        @Query("session_id") sessionId: String?,
        @Query("page") page: Int
    ): Response<ApiResponse<CustomMovieListResponse>>

}
