package com.london.data.datasource.local

import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal

interface LocalDataSource {
    suspend fun insertMovie(value : SearchMoviesLocal)
    suspend fun insertTvShow(value : SearchTvShowLocal)
    suspend fun insertActor(value : SearchActorsLocal)
    suspend fun updateMovie(value: SearchMoviesLocal)
    suspend fun updateTvShow(value: SearchTvShowLocal)
    suspend fun updateActor(value: SearchActorsLocal)
    suspend fun deleteMovie(value: SearchMoviesLocal)
    suspend fun deleteTvShow(value: SearchTvShowLocal)
    suspend fun deleteActor(value: SearchActorsLocal)
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