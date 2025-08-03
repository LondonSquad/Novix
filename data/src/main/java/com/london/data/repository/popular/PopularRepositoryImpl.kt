package com.london.data.repository.popular

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.source.HomeLocalDataSource
import com.london.data.mapper.popular.toMovieEntity
import com.london.data.mapper.popular.toPopularMovieSectionLocal
import com.london.data.mapper.popular.toPopularMovies
import com.london.data.mapper.popular.toPopularTvShowSectionLocal
import com.london.data.mapper.popular.toPopularTvShows
import com.london.data.mapper.popular.toTvShowEntity
import com.london.data.remote.source.home.popular.PopularRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.popular.PopularTvShow
import com.london.domain.entity.recent.MediaType
import com.london.domain.repository.PopularRepository
import javax.inject.Inject

class PopularRepositoryImpl @Inject constructor(
    private val popularRemoteDataSource: PopularRemoteDataSource,
    private val homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal>,
    private val crashReporter: CrashReporter
) : PopularRepository {

    override suspend fun getPopularMovies(): List<PopularMovie> = fetchAndSync(
        cacheBlock = {
            val local = homeLocalDataSource.getAll()
                .filter { it.mediaType == MediaType.Movie }
                .map { it.toMovieEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            popularRemoteDataSource.getPopularMovies().getOrThrow().toPopularMovies()
        },
        syncBlock = { popularList ->
            homeLocalDataSource.insertAll(popularList.map { it.toPopularMovieSectionLocal(MediaType.Movie) })
        },
        crashReporter = crashReporter
    )

    override suspend fun getPopularTvShows(): List<PopularTvShow> = fetchAndSync(
        cacheBlock = {
            val local = homeLocalDataSource.getAll()
                .filter { it.mediaType == MediaType.TvShow }
                .map { it.toTvShowEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            popularRemoteDataSource.getPopularTvShows().getOrThrow().toPopularTvShows()
        },
        syncBlock = { popularList ->
            homeLocalDataSource.insertAll(popularList.map { it.toPopularTvShowSectionLocal(MediaType.TvShow) })
        },
        crashReporter = crashReporter
    )
}
