package com.london.data.datasource.local.localDataSourceImpl

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.util.executeDelete
import com.london.data.datasource.util.executeGetAll
import com.london.data.datasource.util.executeGetByDate
import com.london.data.datasource.util.executeGetByQuery
import com.london.data.datasource.util.executeInsert
import com.london.data.datasource.util.executeUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorLocalDataSourceImpl(
    private val searchActorsDao: SearchActorsDao
) : LocalDataSource<SearchActorsLocal> {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            searchActorsDao.getAll().forEach {
                if (isOneHourExpired(it.date)) searchActorsDao.delete(it)
            }
        }
    }

    override suspend fun insert(item: SearchActorsLocal) = searchActorsDao.executeInsert(item)

    override suspend fun update(item: SearchActorsLocal) = searchActorsDao.executeUpdate(item)

    override suspend fun delete(item: SearchActorsLocal) = searchActorsDao.executeDelete(item)

    override suspend fun get(): List<SearchActorsLocal> = searchActorsDao.executeGetAll()

    override suspend fun getByDate(date: Long): SearchActorsLocal =
        searchActorsDao.executeGetByDate(date)

    override suspend fun getByQuery(query: String): SearchActorsLocal =
        searchActorsDao.executeGetByQuery(query.generateHash())
}