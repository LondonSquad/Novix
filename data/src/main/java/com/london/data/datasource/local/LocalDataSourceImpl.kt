package com.london.data.datasource.local

import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.dto.SearchActorsResponse
import com.london.data.datasource.local.dto.SearchMoviesResponseLocal
import com.london.data.datasource.local.dto.SearchTvShowsResponseLocal

class LocalDataSourceImpl(
    private val searchTvShowDao: SearchTvShowDao,
    private val searchMoviesDao: SearchMoviesDao,
    private val searchActorsDao: SearchActorsDao
) : LocalDataSource {
    override fun insertMovie(movie: SearchMoviesResponseLocal) =
        runCatching { searchMoviesDao.insert(movie) }.getOrElse { throw InsertMovieExpansion() }

    override fun insertTvShow(tvShow: SearchTvShowsResponseLocal) =
        runCatching { searchTvShowDao.insert(tvShow) }.getOrElse { throw InsertTvShowExpansion() }

    override fun insertActor(actor: SearchActorsResponse) =
        runCatching { searchActorsDao.insert(actor) }.getOrElse { throw InsertActorExpansion() }

    override fun updateMovie(movie: SearchMoviesResponseLocal) =
        runCatching { searchMoviesDao.update(movie) }.getOrElse { throw UpdateMovieExpansion() }

    override fun updateTvShow(tvShow: SearchTvShowsResponseLocal) =
        runCatching { searchTvShowDao.update(tvShow) }.getOrElse { throw UpdateTvShowExpansion() }

    override fun updateActor(actor: SearchActorsResponse) =
        runCatching { searchActorsDao.update(actor) }.getOrElse { throw UpdateActorExpansion() }

    override fun deleteMovie(movie: SearchMoviesResponseLocal) =
        runCatching { searchMoviesDao.delete(movie) }.getOrElse { throw DeleteMovieExpansion() }

    override fun deleteTvShow(tvShow: SearchTvShowsResponseLocal) =
        runCatching { searchTvShowDao.delete(tvShow) }.getOrElse { throw DeleteTvShowExpansion() }

    override fun deleteActor(actor: SearchActorsResponse) =
        runCatching { searchActorsDao.delete(actor) }.getOrElse { throw DeleteActorExpansion() }

    override fun getMovies(): SearchMoviesResponseLocal =
        runCatching { searchMoviesDao.getAll() }.getOrElse { throw GetMoviesAllExpansion() }

    override fun getTvShows(): SearchTvShowsResponseLocal =
        runCatching { searchTvShowDao.getAll() }.getOrElse { throw GetTvShowsAllExpansion() }

    override fun getActors(): SearchActorsResponse=
        runCatching { searchActorsDao.getAll() }.getOrElse { throw GetActorsAllExpansion() }

    override fun getMovieByDate(date: Long): SearchMoviesResponseLocal =
        runCatching { searchMoviesDao.getCurrentSearch(date) }.getOrElse { throw GetMovieByDateExpansion() }

    override fun getTvShowByDate(date: Long): SearchTvShowsResponseLocal =
        kotlin.runCatching { searchTvShowDao.getCurrentSearch(date) }
            .getOrElse { throw GetTvShowByDateExpansion() }

    override fun getActorByDate(date: Long): SearchActorsResponse =
        kotlin.runCatching { searchActorsDao.getCurrentSearch(date) }
            .getOrElse { throw GetActorByDateExpansion() }
}