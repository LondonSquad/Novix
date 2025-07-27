package com.london.data.local.source.search

import com.london.data.local.source.LocalDataSource
import com.london.data.local.database.dao.search.SearchMoviesDao
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.local.utils.executeDelete
import com.london.data.local.utils.executeGetAll
import com.london.data.local.utils.executeGetByDate
import com.london.data.local.utils.executeGetByQuery
import com.london.data.local.utils.executeGetByQueryAndPage
import com.london.data.local.utils.executeInsert
import com.london.data.local.utils.executeUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named("movieLocalDataSource")
class MovieLocalDataSourceImpl(
    private val searchMoviesDao: SearchMoviesDao
) : LocalDataSource<SearchMoviesLocal> {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            searchMoviesDao.getAll().forEach {
                if (isOneHourExpired(it.date)) searchMoviesDao.delete(it)
            }
        }
    }

    override suspend fun insert(item: SearchMoviesLocal) = searchMoviesDao.executeInsert(item)

    override suspend fun update(item: SearchMoviesLocal) = searchMoviesDao.executeUpdate(item)

    override suspend fun delete(item: SearchMoviesLocal) = searchMoviesDao.executeDelete(item)

    override suspend fun get(): List<SearchMoviesLocal> = searchMoviesDao.executeGetAll()

    override suspend fun getByDate(date: Long): SearchMoviesLocal =
        searchMoviesDao.executeGetByDate(date)

    override suspend fun getByQuery(query: String): SearchMoviesLocal? {
        return try {
            searchMoviesDao.executeGetByQuery(query.generateHash())
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getByQueryAndPage(query: String, page: Int): SearchMoviesLocal? {
        return try {
            searchMoviesDao.executeGetByQueryAndPage(query.generateHash(), page)
        } catch (_: Exception) {
            null
        }

    }

}