package com.london.data.datasource.local

import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal

class LocalDataSourceImpl(
    private val searchTvShowDao: SearchTvShowDao,
    private val searchMoviesDao: SearchMoviesDao,
    private val searchActorsDao: SearchActorsDao
) : LocalDataSource {
    override suspend fun insertMovie(movie: SearchMoviesLocal) =
       runOrThrow({ searchMoviesDao.insert(movie) }, ::InsertMovieExpansion)

    override suspend fun insertTvShow(tvShow: SearchTvShowLocal) =
        runOrThrow({ searchTvShowDao.insert(tvShow) }, ::InsertTvShowExpansion)

    override suspend fun insertActor(actor: SearchActorsLocal) =
        runOrThrow({ searchActorsDao.insert(actor) }, ::InsertActorExpansion)

    override suspend fun updateMovie(movie: SearchMoviesLocal) =
        runOrThrow({ searchMoviesDao.update(movie) }, ::UpdateMovieExpansion)

    override suspend fun updateTvShow(tvShow: SearchTvShowLocal) =
        runOrThrow({ searchTvShowDao.update(tvShow) }, ::UpdateTvShowExpansion)

    override suspend fun updateActor(actor: SearchActorsLocal) =
        runOrThrow({ searchActorsDao.update(actor) }, ::UpdateActorExpansion)

    override suspend fun deleteMovie(movie: SearchMoviesLocal) =
        runOrThrow({ searchMoviesDao.delete(movie) }, ::DeleteMovieExpansion)

    override suspend fun deleteTvShow(tvShow: SearchTvShowLocal) =
        runOrThrow({ searchTvShowDao.delete(tvShow) }, ::DeleteTvShowExpansion)

    override suspend fun deleteActor(actor: SearchActorsLocal) =
        runOrThrow({ searchActorsDao.delete(actor) }, ::DeleteActorExpansion)

    override suspend fun getMovies(): SearchMoviesLocal =
        runOrThrow({ searchMoviesDao.getAll() }, ::GetMoviesAllExpansion)

    override suspend fun getTvShows(): SearchTvShowLocal =
        runOrThrow({ searchTvShowDao.getAll() }, ::GetTvShowsAllExpansion)

    override suspend fun getActors(): SearchActorsLocal =
        runOrThrow({ searchActorsDao.getAll() }, ::GetActorsAllExpansion)

    override suspend fun getMovieByDate(date: Long): SearchMoviesLocal =
        runOrThrow({ searchMoviesDao.getCurrentSearch(date) }, ::GetMovieByDateExpansion)

    override suspend fun getTvShowByDate(date: Long): SearchTvShowLocal =
        runOrThrow({ searchTvShowDao.getCurrentSearch(date) }, ::GetTvShowByDateExpansion)

    override suspend fun getActorByDate(date: Long): SearchActorsLocal =
        runOrThrow({ searchActorsDao.getCurrentSearch(date) }, ::GetActorByDateExpansion)

    override suspend fun getActorByQuery(query: String): SearchActorsLocal =
        runOrThrow({ searchActorsDao.getSearchByQuery(query) }, ::GetActorByQueryExpansion)

    override suspend fun getTvShowByQuery(query: String): SearchTvShowLocal =
        runOrThrow({ searchTvShowDao.getSearchByQuery(query) }, ::GetTvShowByQueryExpansion)

    override suspend fun getMovieByQuery(query: String): SearchMoviesLocal =
        runOrThrow({ searchMoviesDao.getSearchByQuery(query) }, ::GetMovieByQueryExpansion)
}

suspend inline fun <T> runOrThrow(
    crossinline block: suspend () -> T,
    crossinline error: () -> Throwable
): T = runCatching { block() }.getOrElse { throw error() }
