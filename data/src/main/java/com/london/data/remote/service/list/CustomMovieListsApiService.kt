package com.london.data.remote.service.list

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.list.CreateCustomListBody
import com.london.data.remote.model.list.CreateCustomListResponse
import com.london.data.remote.model.list.CustomListResponse
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.model.list.ListMovieBody
import com.london.data.remote.model.search.MovieRemote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomMovieListsApiService {

    @POST("3/list")
    suspend fun create(
        @Query("session_id") sessionId: String?,
        @Body createCustomListBody: CreateCustomListBody
    ): Response<CreateCustomListResponse>

    @POST("3/list/{list_id}")
    suspend fun delete(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?
    ): Response<CustomListResponse>

    @GET("3/list/{list_id}")
    suspend fun getDetails(
        @Path("list_id") listId: Int,
        @Query("page") page: Int
    ): Response<ApiResponse<MovieRemote>>

    @POST("3/list/{list_id}/add_item")
    suspend fun addMovieToList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?,
        @Body movieAdditionBody: ListMovieBody
    ): Response<CustomListResponse>

    @GET("3/list/{list_id}/remove_item")
    suspend fun removeMovieFromList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?,
        @Body movieDeletionBody: ListMovieBody
    ): Response<CustomListResponse>

    @GET("3/account/{account_id}/lists")
    suspend fun getAllUserLists(
        @Query("session_id") sessionId: String?
    ): Response<ApiResponse<CustomMovieListResponse>>
}
