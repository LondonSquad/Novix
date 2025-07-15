package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie

interface MovieDetailsRepository {
    suspend fun getMovieUsingId(id: Int): MovieDetails
    suspend fun getSimilarMoviesUsingId(id: Int): List<SimilarMovie>
    suspend fun getMovieImagesUsingId(id: Int): List<String>
    suspend fun getMovieCastUsingId(id: Int): List<Actor>
}