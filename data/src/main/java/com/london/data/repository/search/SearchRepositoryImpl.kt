package com.london.data.repository.search

import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.model.search.GenreInterestEntity
import com.london.data.mapper.search.toEntity
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val genreInterestDao: GenreInterestDao,
    private val remoteDataSource: SearchRemoteDataSource,
    private val crashReporter: CrashReporter
) : SearchRepository {

    override suspend fun searchForMovies(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<Movie> {
            val response = remoteDataSource.searchForMovies(
                query = name,
                includeAdult = false,
                pageNumber = pageNumber,
            ).getOrThrow()

        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun searchForTvShows(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<TvShow> {
            val response = remoteDataSource.searchForTvShows(
                query = name,
                includeAdult = false,
                pageNumber = pageNumber,
            ).getOrThrow()

        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun searchForActors(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<Actor> {
            val response = remoteDataSource.searchForActors(
                query = name,
                includeAdult = false,
                pageNumber = pageNumber,
            ).getOrThrow()

        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun incrementGenreInterest(genreId: Int, mediaType: String) {
        try {
            val current = genreInterestDao.getGenreInterest(genreId, mediaType)
            if (current == null) {
                genreInterestDao.insertGenreInterest(
                    GenreInterestEntity(genreId = genreId, mediaType = mediaType, count = 1)
                )
            } else {
                genreInterestDao.updateGenreInterest(
                    current.copy(count = current.count + 1)
                )
            }
        } catch (e: Exception) {
            crashReporter.logException(e)
        }
    }

    override suspend fun getGenreInterestCounts(mediaType: String): List<Pair<Int, Int>> {
        return try {
            genreInterestDao.getGenresByInterest(mediaType)
                .map { entity -> entity.genreId to entity.count }
        } catch (e: Exception) {
            crashReporter.logException(e)
            emptyList()
        }
    }
}
