package com.london.data.local.source.home.upcoming

import com.london.data.local.database.dao.home.upcoming.UpcomingSectionDao
import com.london.data.local.model.home.upcoming.UpComingSectionLocal
import com.london.data.utils.isDayExpired
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpComingLocalDataSourceImpl @Inject constructor(
    private val upcomingSectionDao: UpcomingSectionDao
): UpComingLocalDataSource{

    init {
        CoroutineScope(Dispatchers.IO).launch {
            upcomingSectionDao.getAll().forEach { upcomingSectionLocal ->
                if (upcomingSectionLocal.date.isDayExpired())
                    upcomingSectionDao.deleteAll()
            }
        }
    }

    override suspend fun insertAll(items: List<UpComingSectionLocal>) =
        upcomingSectionDao.insertAll(items)


    override suspend fun insert(item: UpComingSectionLocal) =
        upcomingSectionDao.insert(item)

    override suspend fun getAll(): List<UpComingSectionLocal> =
        upcomingSectionDao.getAll()

    override suspend fun getByDate(date: Long): UpComingSectionLocal =
        upcomingSectionDao.getByDate(date)

    override suspend fun getUpComingMoviesPage(page: Int): UpComingSectionLocal =
        upcomingSectionDao.getUpComingMoviesPage(page)

}