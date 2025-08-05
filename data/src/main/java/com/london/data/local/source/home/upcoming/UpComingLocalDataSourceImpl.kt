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
        deleteExpiredData()
    }

    override suspend fun insert(item: UpComingSectionLocal) =
        upcomingSectionDao.insert(item)

    override suspend fun getUpComingMoviesPage(categoryId: Int?, page: Int): UpComingSectionLocal =
        upcomingSectionDao.getUpComingMoviesPage(categoryId, page)

    private fun deleteExpiredData(){
        CoroutineScope(Dispatchers.IO).launch {
            upcomingSectionDao.getAll().forEach { upcomingSectionLocal ->
                if (upcomingSectionLocal.date.isDayExpired())
                    upcomingSectionDao.deleteAll()
            }
        }
    }
}
