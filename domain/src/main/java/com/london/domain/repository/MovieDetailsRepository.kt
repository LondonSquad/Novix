package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie


interface MovieDetailsRepository {
    suspend fun getMovieById(id: Int): MovieDetails
    suspend fun getSimilarMoviesById(id: Int): List<SimilarMovie>
    suspend fun getMovieImagesById(id: Int): List<String>
    suspend fun getMovieCastById(id: Int): List<Actor>
}