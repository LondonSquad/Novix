package com.london.data.local.source.home.toprated

import com.london.data.local.database.dao.home.toprated.TopRatedDao
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.local.utils.executeInsert
import com.london.data.utils.isDayExpired
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopRatedDataSourceImpl @Inject constructor (
    private val topRatedDao: TopRatedDao
) : HomeLocalDataSource<TopRatedLocal> {

    init {
        deleteExpiredData()
    }

    override suspend fun insert(item: TopRatedLocal) =
        topRatedDao.executeInsert(item)


    override suspend fun insertAll(items: List<TopRatedLocal>) =
        topRatedDao.insertAll(items)

    override suspend fun deleteAll() =
        topRatedDao.deleteAll()

    override suspend fun getAll(): List<TopRatedLocal> =
        topRatedDao.getAll()

    override suspend fun getByDate(date: Long): TopRatedLocal =
        topRatedDao.getByDate(date)
    
    private fun deleteExpiredData() {
        CoroutineScope(Dispatchers.IO).launch {
            topRatedDao.getAll().forEach { popularLocal ->
                if (popularLocal.date.isDayExpired())
                    topRatedDao.deleteAll()
            }
        }
    }
}

