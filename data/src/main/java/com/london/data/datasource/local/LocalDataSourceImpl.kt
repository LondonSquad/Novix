package com.london.data.datasource.local

import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.dto.SearchActorsResponseLocal
import com.london.data.datasource.local.dto.SearchMoviesResponseLocal
import com.london.data.datasource.local.dto.SearchTvShowsResponseLocal


class LocalDataSourceImpl(
    private val searchTvShowDao: SearchTvShowDao,
    private val searchMoviesDao: SearchMoviesDao,
    private val searchActorsDao: SearchActorsDao
) : LocalDataSource {
    override suspend fun insertMovie(movie: SearchMoviesResponseLocal) =
        runCatching { searchMoviesDao.insert(movie) }.getOrElse { throw InsertMovieExpansion() }

    override suspend fun insertTvShow(tvShow: SearchTvShowsResponseLocal) =
        runCatching { searchTvShowDao.insert(tvShow) }.getOrElse { throw InsertTvShowExpansion() }

    override suspend fun insertActor(actor: SearchActorsResponseLocal) =
        runCatching { searchActorsDao.insert(actor) }.getOrElse { throw InsertActorExpansion() }

    override suspend fun updateMovie(movie: SearchMoviesResponseLocal) =
        runCatching { searchMoviesDao.update(movie) }.getOrElse { throw UpdateMovieExpansion() }

    override suspend fun updateTvShow(tvShow: SearchTvShowsResponseLocal) =
        runCatching { searchTvShowDao.update(tvShow) }.getOrElse { throw UpdateTvShowExpansion() }

    override suspend fun updateActor(actor: SearchActorsResponseLocal) =
        runCatching { searchActorsDao.update(actor) }.getOrElse { throw UpdateActorExpansion() }

    override suspend fun deleteMovie(movie: SearchMoviesResponseLocal) =
        runCatching { searchMoviesDao.delete(movie) }.getOrElse { throw DeleteMovieExpansion() }

    override suspend fun deleteTvShow(tvShow: SearchTvShowsResponseLocal) =
        runCatching { searchTvShowDao.delete(tvShow) }.getOrElse { throw DeleteTvShowExpansion() }

    override suspend fun deleteActor(actor: SearchActorsResponseLocal) =
        runCatching { searchActorsDao.delete(actor) }.getOrElse { throw DeleteActorExpansion() }

    override suspend fun getMovies(): SearchMoviesResponseLocal =
        runCatching { searchMoviesDao.getAll() }.getOrElse { throw GetMoviesAllExpansion() }

    override suspend fun getTvShows(): SearchTvShowsResponseLocal =
        runCatching { searchTvShowDao.getAll() }.getOrElse { throw GetTvShowsAllExpansion() }

    override suspend fun getActors(): SearchActorsResponseLocal =
        runCatching { searchActorsDao.getAll() }.getOrElse { throw GetActorsAllExpansion() }

    override suspend fun getMovieByDate(date: Long): SearchMoviesResponseLocal =
        runCatching { searchMoviesDao.getCurrentSearch(date) }
            .getOrElse { throw GetMovieByDateExpansion() }

    override suspend fun getTvShowByDate(date: Long): SearchTvShowsResponseLocal =
        kotlin.runCatching { searchTvShowDao.getCurrentSearch(date) }
            .getOrElse { throw GetTvShowByDateExpansion() }

    override suspend fun getActorByDate(date: Long): SearchActorsResponseLocal =
        kotlin.runCatching { searchActorsDao.getCurrentSearch(date) }
            .getOrElse { throw GetActorByDateExpansion() }

    override suspend fun getActorByQuery(query: String): SearchActorsResponseLocal =
        runCatching { searchActorsDao.getSearchByQuery(query) }
            .getOrElse { throw GetActorByQueryExpansion() }

    override suspend fun getTvShowByQuery(query: String): SearchTvShowsResponseLocal =
        runCatching { searchTvShowDao.getSearchByQuery(query) }
            .getOrElse { throw GetTvShowByQueryExpansion() }

    override suspend fun getMovieByQuery(query: String): SearchMoviesResponseLocal =
        runCatching { searchMoviesDao.getSearchByQuery(query) }
            .getOrElse { throw GetMovieByQueryExpansion() }
}