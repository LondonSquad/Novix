package com.london.data.datasource.local

import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.util.executeDelete
import com.london.data.datasource.util.executeGetAll
import com.london.data.datasource.util.executeGetByDate
import com.london.data.datasource.util.executeGetByQuery
import com.london.data.datasource.util.executeInsert
import com.london.data.datasource.util.executeUpdate

class LocalDataSourceImpl(
    private val searchTvShowDao: SearchTvShowDao,
    private val searchMoviesDao: SearchMoviesDao,
    private val searchActorsDao: SearchActorsDao
) : LocalDataSource {
    override suspend fun insertMovie(movie: SearchMoviesLocal) =
        searchMoviesDao.executeInsert(movie)

    override suspend fun insertTvShow(tvShow: SearchTvShowLocal) =
        searchTvShowDao.executeInsert(tvShow)

    override suspend fun insertActor(actor: SearchActorsLocal) =
        searchActorsDao.executeInsert(actor)

    override suspend fun updateMovie(movie: SearchMoviesLocal) =
        searchMoviesDao.executeUpdate(movie)

    override suspend fun updateTvShow(tvShow: SearchTvShowLocal) =
        searchTvShowDao.executeUpdate(tvShow)

    override suspend fun updateActor(actor: SearchActorsLocal) =
        searchActorsDao.executeUpdate(actor)

    override suspend fun deleteMovie(movie: SearchMoviesLocal) =
        searchMoviesDao.executeDelete(movie)

    override suspend fun deleteTvShow(tvShow: SearchTvShowLocal) =
        searchTvShowDao.executeDelete(tvShow)

    override suspend fun deleteActor(actor: SearchActorsLocal) =
        searchActorsDao.executeDelete(actor)

    override suspend fun getMovies(): List<SearchMoviesLocal> = searchMoviesDao.executeGetAll()

    override suspend fun getTvShows(): List<SearchTvShowLocal> = searchTvShowDao.executeGetAll()

    override suspend fun getActors(): List<SearchActorsLocal> = searchActorsDao.executeGetAll()

    override suspend fun getMovieByDate(date: Long): SearchMoviesLocal =
        searchMoviesDao.executeGetByDate(date)

    override suspend fun getTvShowByDate(date: Long): SearchTvShowLocal =
        searchTvShowDao.executeGetByDate(date)

    override suspend fun getActorByDate(date: Long): SearchActorsLocal =
        searchActorsDao.executeGetByDate(date)

    override suspend fun getActorByQuery(query: String): SearchActorsLocal =
        searchActorsDao.executeGetByQuery(query)

    override suspend fun getTvShowByQuery(query: String): SearchTvShowLocal =
        searchTvShowDao.executeGetByQuery(query)

    override suspend fun getMovieByQuery(query: String): SearchMoviesLocal =
        searchMoviesDao.executeGetByQuery(query)
}