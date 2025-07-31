package com.london.data.local.source.search

import com.london.data.local.database.dao.search.SearchMoviesDao
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.local.source.LocalDataSource
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
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val searchMoviesDao: SearchMoviesDao
) : LocalDataSource<SearchMoviesLocal> {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            searchMoviesDao.getAll().forEach { searchMoviesLocal ->
                if (searchMoviesLocal.date.isOneHourExpired())
                    searchMoviesDao.delete(
                    searchMoviesLocal
                )
            }
        }
    }

    override suspend fun insert(item: SearchMoviesLocal) = searchMoviesDao.executeInsert(item)

    override suspend fun update(item: SearchMoviesLocal) = searchMoviesDao.executeUpdate(item)

    override suspend fun delete(item: SearchMoviesLocal) = searchMoviesDao.executeDelete(item)

    override suspend fun get(): List<SearchMoviesLocal> = searchMoviesDao.executeGetAll()

    override suspend fun getByDate(date: Long): SearchMoviesLocal =
        searchMoviesDao.executeGetByDate(date)

    override suspend fun getByQuery(query: String): SearchMoviesLocal? = runCatching {
        searchMoviesDao.executeGetByQuery(query.generateHash())
    }.getOrNull()


    override suspend fun getByQueryAndPage(query: String, page: Int): SearchMoviesLocal? =
        runCatching {
            searchMoviesDao.executeGetByQueryAndPage(query.generateHash(), page)
        }.getOrNull()
}
