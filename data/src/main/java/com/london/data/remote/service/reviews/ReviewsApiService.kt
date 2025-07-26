package com.london.data.remote.service.reviews

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.reviews.model.ReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewsApiService {

    @GET("3/movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): Response<ApiResponse<ReviewResponse>>

    @GET("3/tv/{tv_id}/reviews")
    suspend fun getTvShowReviews(
        @Path("tv_id") tvShowId: Int,
        @Query("page") page: Int
    ): Response<ApiResponse<ReviewResponse>>
}