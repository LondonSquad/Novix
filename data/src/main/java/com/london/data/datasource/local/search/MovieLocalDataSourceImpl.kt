package com.london.data.datasource.local.search

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.dao.search.SearchMoviesDao
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.util.executeDelete
import com.london.data.datasource.util.executeGetAll
import com.london.data.datasource.util.executeGetByDate
import com.london.data.datasource.util.executeGetByQuery
import com.london.data.datasource.util.executeGetByQueryAndPage
import com.london.data.datasource.util.executeInsert
import com.london.data.datasource.util.executeUpdate
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