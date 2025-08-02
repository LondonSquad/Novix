package com.london.data.repository.popular

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.source.HomeLocalDataSource
import com.london.data.mapper.popular.toEntity
import com.london.data.mapper.popular.toPopularMovies
import com.london.data.mapper.popular.toPopularSectionLocal
import com.london.data.remote.source.home.popular.PopularRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.popular.PopularTvShow
import com.london.domain.repository.PopularRepository
import javax.inject.Inject

class PopularRepositoryImpl @Inject constructor(
    private val popularRemoteDataSource: PopularRemoteDataSource,
    private val homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>,
    private val crashReporter: CrashReporter
) : PopularRepository {

    //    override suspend fun getPopularMovies(): List<PopularMovie> {
//        return popularRemoteDataSource.getPopularMovies().getOrThrow().toPopularMovies()
//    }
//
//    override suspend fun getPopularTvShows(): List<PopularTvShow> {
//        return popularRemoteDataSource.getPopularTvShows().getOrThrow().toEntityList()
//    }
    override suspend fun getPopularMovies(): List<PopularMovie> = fetchAndSync(
        cacheBlock = { homeLocalDataSource.getAll().map { it.toEntity() } },
        networkBlock = { popularRemoteDataSource.getPopularMovies().getOrThrow().toPopularMovies() },
        syncBlock = { popularList ->
            homeLocalDataSource.insertAll(popularList.map { it.toPopularSectionLocal(popularList) }) },
        crashReporter = crashReporter
    )

    override suspend fun getPopularTvShows(): List<PopularTvShow> {
        TODO("Not yet implemented")
    }

}