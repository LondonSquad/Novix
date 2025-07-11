package com.london.data.datasource.local

import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.utils.extensions.passArgToMessage
import com.london.data.utils.extensions.runOrThrow


class LocalDataSourceImpl(
    private val searchTvShowDao: SearchTvShowDao,
    private val searchMoviesDao: SearchMoviesDao,
    private val searchActorsDao: SearchActorsDao
) : LocalDataSource {
    override suspend fun insertMovie(value: SearchMoviesLocal) = searchMoviesDao.executeInsert(value)
    override suspend fun insertTvShow(value: SearchTvShowLocal) = searchTvShowDao.executeInsert(value)
    override suspend fun insertActor(value: SearchActorsLocal) = searchActorsDao.executeInsert(value)

    override suspend fun updateMovie(value: SearchMoviesLocal) = searchMoviesDao.executeUpdate(value)
    override suspend fun updateTvShow(value: SearchTvShowLocal) = searchTvShowDao.executeUpdate(value)
    override suspend fun updateActor(value: SearchActorsLocal) = searchActorsDao.executeUpdate(value)

    override suspend fun deleteMovie(value: SearchMoviesLocal) = searchMoviesDao.executeDelete(value)
    override suspend fun deleteTvShow(value: SearchTvShowLocal) = searchTvShowDao.executeDelete(value)
    override suspend fun deleteActor(value: SearchActorsLocal) = searchActorsDao.executeDelete(value)

    override suspend fun getMovies() = searchMoviesDao.executeGet()
    override suspend fun getTvShows() = searchTvShowDao.executeGet()
    override suspend fun getActors() = searchActorsDao.executeGet()

    override suspend fun getMovieByDate(date: Long) = searchMoviesDao.executeGet(date)
    override suspend fun getTvShowByDate(date: Long) = searchTvShowDao.executeGet(date)
    override suspend fun getActorByDate(date: Long) = searchActorsDao.executeGet(date)

    override suspend fun getActorByQuery(query: String) = searchActorsDao.executeGet(query)
    override suspend fun getTvShowByQuery(query: String) = searchTvShowDao.executeGet(query)
    override suspend fun getMovieByQuery(query: String) = searchMoviesDao.executeGet(query)
}

private suspend fun <T : Any> SearchDao<T>.execute(
    input: Any?,
    block: suspend () -> T,
    exception: BaseException,
) = runOrThrow(
    block = { block() },
    onFailure = { exception.passArgToMessage(input ?: this) }
)

@Suppress("UnusedReceiverParameter")
private suspend fun <T : Any> SearchDao<T>.execute(
    input: T,
    block: suspend (T) -> Unit,
    exception: BaseException,
) = runOrThrow(
    block = { block(input) },
    onFailure = { exception.passArgToMessage(input) }
)

private suspend fun <T : Any> SearchDao<T>.executeInsert(input: T): Unit = execute(
    input = input,
    block = ::insert,
    exception = InsertException()
)

private suspend fun <T : Any> SearchDao<T>.executeDelete(input: T) = execute(
    input = input,
    block = ::delete,
    exception = DeleteException()
)

private suspend fun <T : Any> SearchDao<T>.executeUpdate(input: T) = execute(
    input = input,
    block = ::update,
    exception = UpdateException()
)

private suspend fun <T : Any> SearchDao<T>.executeGet(input: Any? = null): T = execute(
    input = input,
    block = ::getAll,
    exception = GetException()
)
