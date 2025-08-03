package com.london.data.local.source.home.popular

import com.london.data.local.database.dao.home.popular.PopularSectionDao
import com.london.data.local.model.home.PopularSectionLocal
import com.london.data.local.utils.executeInsert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularLocalDataSourceImpl @Inject constructor(
    private val popularSectionDao: PopularSectionDao
) : HomeLocalDataSource<PopularSectionLocal> {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            popularSectionDao.getAll().forEach { popularLocal ->
                if (popularLocal.date.isDayExpired())
                    popularSectionDao.deleteAll()
            }
        }
    }

    override suspend fun insert(item: PopularSectionLocal) =
        popularSectionDao.executeInsert(item)

    override suspend fun deleteAll() =
        popularSectionDao.deleteAll()

    override suspend fun getAll(): List<PopularSectionLocal> =
        popularSectionDao.getAll()

    override suspend fun insertAll(items: List<PopularSectionLocal>) =
        popularSectionDao.insertAll(items)

    override suspend fun getCurrentPopularByDate(date: Long): PopularSectionLocal =
        popularSectionDao.getCurrentPopularByDate(date)

}
