package com.london.data.local.source.home.popular

import com.london.data.local.database.dao.home.popular.PopularSectionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PopularLocalDataSource(
    private val popularSectionDao: PopularSectionDao
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            popularSectionDao.getAll().forEach { popularLocal ->
                if (popularLocal.date.isOneHourExpired())
                    popularSectionDao.delete(
                        popularLocal
                    )
            }
        }
    }
}