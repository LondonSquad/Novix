package com.london.data.local.source.home.upcoming

import com.london.data.local.model.home.upcoming.UpComingSectionLocal

interface UpComingLocalDataSource {
    suspend fun insert(item: UpComingSectionLocal)
    suspend fun insertAll(items: List<UpComingSectionLocal>)
    suspend fun getAll(): List<UpComingSectionLocal>
    suspend fun getByDate(date: Long): UpComingSectionLocal
    suspend fun getUpComingMoviesPage(categoryId: Int?, page: Int): UpComingSectionLocal
}