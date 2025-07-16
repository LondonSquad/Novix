package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow

interface SearchRepository {
    suspend fun searchForMovies(name: String, language: String): List<Movie>
    suspend fun searchForTvShows(name: String, language: String): List<TvShow>
    suspend fun searchForActors(name: String, language: String): List<Actor>
    suspend fun incrementGenreInterest(genreId: Int, genreType: String)
    suspend fun getGenreInterestCounts(genreType: String): List<Pair<Int, Int>>
}