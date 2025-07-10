package com.london.data.datasource.local

import com.london.data.datasource.local.dto.SearchMoviesResponseLocal
import com.london.data.datasource.local.dto.SearchTvShowsResponseLocal
import com.london.data.datasource.local.dto.SearchActorsResponse

interface LocalDataSource {
    fun insertMovie(movie : SearchMoviesResponseLocal)
    fun insertTvShow(tvShow : SearchTvShowsResponseLocal)
    fun insertActor(actor : SearchActorsResponse)
    fun updateMovie(movie: SearchMoviesResponseLocal)
    fun updateTvShow(tvShow: SearchTvShowsResponseLocal)
    fun updateActor(actor: SearchActorsResponse)
    fun deleteMovie(movie: SearchMoviesResponseLocal)
    fun deleteTvShow(tvShow: SearchTvShowsResponseLocal)
    fun deleteActor(actor: SearchActorsResponse)
    fun getMovies() : SearchMoviesResponseLocal
    fun getTvShows() : SearchTvShowsResponseLocal
    fun getActors() : SearchActorsResponse
    fun getMovieByDate(date : Long) : SearchMoviesResponseLocal
    fun getTvShowByDate(date : Long) : SearchTvShowsResponseLocal
    fun getActorByDate(date : Long) : SearchActorsResponse
}