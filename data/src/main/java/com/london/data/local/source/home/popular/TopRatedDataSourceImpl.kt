package com.london.data.local.source.home.popular

import com.london.data.local.database.dao.home.popular.TopRatedDao
import com.london.data.local.model.home.TopRatedLocal
import com.london.data.local.utils.executeInsert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopRatedDataSourceImpl @Inject constructor (
    private val topRatedDao: TopRatedDao
) : HomeLocalDataSource<TopRatedLocal> {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            topRatedDao.getAll().forEach { popularLocal ->
                if (popularLocal.date.isDayExpired())
                    topRatedDao.deleteAll()
            }
        }
    }

    override suspend fun insert(item: TopRatedLocal) =
        topRatedDao.executeInsert(item)


    override suspend fun insertAll(items: List<TopRatedLocal>) =
        topRatedDao.insertAll(items)

    override suspend fun deleteAll() =
        topRatedDao.deleteAll()

    override suspend fun getAll(): List<TopRatedLocal> =
        topRatedDao.getAll()

    override suspend fun getCurrentPopularByDate(date: Long): TopRatedLocal =
        topRatedDao.getCurrentPopularByDate(date)

}
