package com.london.data.local.source.search

import com.london.data.local.database.dao.search.SearchActorsDao
import com.london.data.local.model.search.SearchActorsLocal
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
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single


@Single
@Named("actorLocalDataSource")
class ActorLocalDataSourceImpl(
    private val searchActorsDao: SearchActorsDao
) : LocalDataSource<SearchActorsLocal> {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            searchActorsDao.getAll().forEach { searchActorsLocal ->
                if (searchActorsLocal.date.isOneHourExpired())
                    searchActorsDao.delete(
                        searchActorsLocal
                    )
            }
        }
    }

    override suspend fun insert(item: SearchActorsLocal) = searchActorsDao.executeInsert(item)

    override suspend fun update(item: SearchActorsLocal) = searchActorsDao.executeUpdate(item)

    override suspend fun delete(item: SearchActorsLocal) = searchActorsDao.executeDelete(item)

    override suspend fun get(): List<SearchActorsLocal> = searchActorsDao.executeGetAll()

    override suspend fun getByDate(date: Long): SearchActorsLocal =
        searchActorsDao.executeGetByDate(date)

    override suspend fun getByQuery(query: String): SearchActorsLocal? = runCatching {
        searchActorsDao.executeGetByQuery(query.generateHash())
    }.getOrNull()

    override suspend fun getByQueryAndPage(query: String, page: Int): SearchActorsLocal? =
        runCatching {
            searchActorsDao.executeGetByQueryAndPage(query.generateHash(), page)
        }.getOrNull()

}