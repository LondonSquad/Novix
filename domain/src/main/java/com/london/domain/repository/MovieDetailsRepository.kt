package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.MovieStates
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.videoprovider.MovieVideo


interface MovieDetailsRepository {
    suspend fun getMovieById(id: Int): MovieDetails
    suspend fun getSimilarMoviesById(id: Int): List<Movie>
    suspend fun getMovieImagesById(id: Int): List<String>
    suspend fun getMovieCastById(id: Int): List<Actor>
    suspend fun getMovieReviews(movieId: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity>
    suspend fun getMovieVideos(movieId: Int): List<MovieVideo>
    suspend fun getAccountMovieStatesById(id: Int): MovieStates
    suspend fun getMovieLists(movieId: UInt): List<UInt>

}