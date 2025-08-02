package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.MovieStates


interface MovieDetailsRepository {
    suspend fun getMovieById(id: Int): MovieDetails
    suspend fun getSimilarMoviesById(id: Int): List<Movie>
    suspend fun getMovieImagesById(id: Int): List<String>
    suspend fun getMovieCastById(id: Int): List<Actor>
    suspend fun getMovieAccountStatesById(
        id: Int,
    ): MovieStates
}