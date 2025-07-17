package com.london.data.datasource.local.search

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.model.SearchTvShowLocal
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
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single


@Named("tvShowLocalDataSource")
@Single
class TvShowLocalDataSourceImpl(
    @Provided
    private val searchTvShowDao: SearchTvShowDao
) : LocalDataSource<SearchTvShowLocal> {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            searchTvShowDao.getAll().forEach {
                if (isOneHourExpired(it.date)) searchTvShowDao.delete(it)
            }
        }
    }

    override suspend fun insert(item: SearchTvShowLocal) = searchTvShowDao.executeInsert(item)

    override suspend fun update(item: SearchTvShowLocal) = searchTvShowDao.executeUpdate(item)

    override suspend fun delete(item: SearchTvShowLocal) = searchTvShowDao.executeDelete(item)

    override suspend fun get() = searchTvShowDao.executeGetAll()

    override suspend fun getByDate(date: Long) = searchTvShowDao.executeGetByDate(date)

    override suspend fun getByQuery(query: String): SearchTvShowLocal? {
        return try {
            searchTvShowDao.executeGetByQuery(query.generateHash())
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getByQueryAndPage(query: String, page: Int): SearchTvShowLocal? {
        return try {
            searchTvShowDao.executeGetByQueryAndPage(query.generateHash(), page)
        } catch (_: Exception) {
            null
        }
    }
}