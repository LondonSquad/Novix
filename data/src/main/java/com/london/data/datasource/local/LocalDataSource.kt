package com.london.data.datasource.local

import com.london.data.datasource.local.dto.SearchActorsResponseLocal
import com.london.data.datasource.local.dto.SearchMoviesResponseLocal
import com.london.data.datasource.local.dto.SearchTvShowsResponseLocal

interface LocalDataSource {
    suspend fun insertMovie(movie : SearchMoviesResponseLocal)
    suspend fun insertTvShow(tvShow : SearchTvShowsResponseLocal)
    suspend fun insertActor(actor : SearchActorsResponseLocal)
    suspend fun updateMovie(movie: SearchMoviesResponseLocal)
    suspend fun updateTvShow(tvShow: SearchTvShowsResponseLocal)
    suspend fun updateActor(actor: SearchActorsResponseLocal)
    suspend fun deleteMovie(movie: SearchMoviesResponseLocal)
    suspend fun deleteTvShow(tvShow: SearchTvShowsResponseLocal)
    suspend fun deleteActor(actor: SearchActorsResponseLocal)
    suspend fun getMovies() : SearchMoviesResponseLocal
    suspend fun getTvShows() : SearchTvShowsResponseLocal
    suspend fun getActors() : SearchActorsResponseLocal
    suspend fun getMovieByDate(date : Long) : SearchMoviesResponseLocal
    suspend fun getTvShowByDate(date : Long) : SearchTvShowsResponseLocal
    suspend fun getActorByDate(date : Long) : SearchActorsResponseLocal
    suspend fun getActorByQuery(query : String) : SearchActorsResponseLocal
    suspend fun getTvShowByQuery(query : String) : SearchTvShowsResponseLocal
    suspend fun getMovieByQuery(query : String) : SearchMoviesResponseLocal
}