package com.london.data.datasource.local

import com.london.data.datasource.local.dto.SearchMoviesResponse
import com.london.data.datasource.local.dto.SearchTvShowsResponse
import com.london.data.datasource.local.dto.SearchActorsResponse

interface LocalDataSource {
    fun insertMovie(movie : SearchMoviesResponse)
    fun insertTvShow(tvShow : SearchTvShowsResponse)
    fun insertActor(actor : SearchActorsResponse)
    fun updateMovie(movie: SearchMoviesResponse)
    fun updateTvShow(tvShow: SearchTvShowsResponse)
    fun updateActor(actor: SearchActorsResponse)
    fun deleteMovie(movie: SearchMoviesResponse)
    fun deleteTvShow(tvShow: SearchTvShowsResponse)
    fun deleteActor(actor: SearchActorsResponse)
    fun getMovies() : SearchMoviesResponse
    fun getTvShows() : SearchTvShowsResponse
    fun getActors() : SearchActorsResponse
    fun getMovieByDate(date : Long) : SearchMoviesResponse
    fun getTvShowByDate(date : Long) : SearchTvShowsResponse
    fun getActorByDate(date : Long) : SearchActorsResponse
}