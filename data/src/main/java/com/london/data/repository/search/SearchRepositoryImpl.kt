package com.london.data.repository.search

import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.model.search.GenreInterestEntity
import com.london.data.mapper.genre.toGenreId
import com.london.data.mapper.search.toEntity
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.model.search.searchactor.SearchActorRemote
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.Genre
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
        val response = getMovieSearchResult(name = name, pageNumber = pageNumber)

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
        val response = getTvShowSearchResult(name = name, pageNumber = pageNumber)

        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    private suspend fun getTvShowSearchResult(
        name: String,
        pageNumber: Int
    ): ApiResponse<SearchTvShowRemote> {
        return remoteDataSource.searchForTvShows(
            query = name,
            includeAdult = false,
            pageNumber = pageNumber,
        ).getOrThrow()
    }

    override suspend fun searchForActors(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<Actor> {
        val response = getActorsSearchResult(name = name, pageNumber = pageNumber)

        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun incrementGenreInterest(genre: Genre, mediaType: String) {
        runCatching {
            val current = genreInterestDao.getGenreInterest(genre.toGenreId(), mediaType)

            if (current == null) insertGenreInterest(genre = genre, mediaType = mediaType)
            else updateGenreInterest(current)
        }.onFailure {
            crashReporter.logException(it)
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

    private suspend fun getMovieSearchResult(
        name: String,
        pageNumber: Int
    ): ApiResponse<MovieRemote> {
        return remoteDataSource.searchForMovies(
            query = name,
            includeAdult = false,
            pageNumber = pageNumber,
        ).getOrThrow()
    }

    private suspend fun getActorsSearchResult(
        name: String,
        pageNumber: Int
    ): ApiResponse<SearchActorRemote> {
        return remoteDataSource.searchForActors(
            query = name,
            includeAdult = false,
            pageNumber = pageNumber,
        ).getOrThrow()
    }

    private suspend fun updateGenreInterest(current: GenreInterestEntity) {
        genreInterestDao.updateGenreInterest(
            current.copy(count = current.count + 1)
        )
    }

    private suspend fun insertGenreInterest(genre: Genre, mediaType: String) {
        genreInterestDao.insertGenreInterest(
            GenreInterestEntity(
                genreId = genre.toGenreId(), mediaType = mediaType, count = 1
            )
        )
    }
}
