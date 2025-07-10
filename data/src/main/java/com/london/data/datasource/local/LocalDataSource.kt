package com.london.data.datasource.local

import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal

interface LocalDataSource {
    suspend fun insertMovie(movie : SearchMoviesLocal)
    suspend fun insertTvShow(tvShow : SearchTvShowLocal)
    suspend fun insertActor(actor : SearchActorsLocal)
    suspend fun updateMovie(movie: SearchMoviesLocal)
    suspend fun updateTvShow(tvShow: SearchTvShowLocal)
    suspend fun updateActor(actor: SearchActorsLocal)
    suspend fun deleteMovie(movie: SearchMoviesLocal)
    suspend fun deleteTvShow(tvShow: SearchTvShowLocal)
    suspend fun deleteActor(actor: SearchActorsLocal)
    suspend fun getMovies() : SearchMoviesLocal
    suspend fun getTvShows() : SearchTvShowLocal
    suspend fun getActors() : SearchActorsLocal
    suspend fun getMovieByDate(date : Long) : SearchMoviesLocal
    suspend fun getTvShowByDate(date : Long) : SearchTvShowLocal
    suspend fun getActorByDate(date : Long) : SearchActorsLocal
    suspend fun getActorByQuery(query : String) : SearchActorsLocal
    suspend fun getTvShowByQuery(query : String) : SearchTvShowLocal
    suspend fun getMovieByQuery(query : String) : SearchMoviesLocal
}