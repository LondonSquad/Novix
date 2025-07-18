package com.london.data.repository

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.dao.GenreInterestDao
import com.london.data.datasource.local.model.GenreInterestEntity
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.search.SearchRemoteDataSource
import com.london.data.datasource.util.CrashReporter
import com.london.data.mapper.toActorEntity
import com.london.data.mapper.toLocal
import com.london.data.mapper.toMovieEntity
import com.london.data.mapper.toTvShowEntity
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class SearchRepositoryImpl(
    @Provided @Named("tvShowLocalDataSource") private val localTvShowDataSource: LocalDataSource<SearchTvShowLocal>,
    @Provided @Named("actorLocalDataSource") private val localActorDataSource: LocalDataSource<SearchActorsLocal>,
    @Provided @Named("movieLocalDataSource") private val localMovieDataSource: LocalDataSource<SearchMoviesLocal>,
    private val genreInterestDao: GenreInterestDao,
    private val remoteDataSource: SearchRemoteDataSource,
    private val crashReporter: CrashReporter
) : SearchRepository {

    suspend fun <T> Result<T?>.getNotNullOrElse(elseBlock: suspend () -> T): Result<T> =
        runCatching { getOrElse { elseBlock() } ?: elseBlock() }

    suspend fun <T> fetchAndSync(
        cacheBlock: (suspend () -> T?)? = null,
        networkBlock: suspend () -> T,
        syncBlock: (suspend (T) -> Unit)? = null,
        crashReporter: CrashReporter? = null
    ): T = runCatching { cacheBlock?.invoke() }.getNotNullOrElse {
            networkBlock().also {
                syncBlock?.invoke(it)
            }
        }.onFailure { crashReporter?.logException(it) }.getOrThrow()

    override suspend fun searchForMovies(
        name: String, language: String, pageNumber: Int
    ): PagedFetchResponse<Movie> = fetchAndSync(
        cacheBlock = {
        localMovieDataSource.getByQueryAndPage(
            query = name + language, page = pageNumber
        )
    }, networkBlock = {
        remoteDataSource.searchForMovies(
            query = name,
            language = language,
            includeAdult = false,
            pageNumber = pageNumber,
        ).toLocal(query = name + language)
    }, syncBlock = { localMovieDataSource.insert(it) }, crashReporter = crashReporter
    ).run {
        PagedFetchResponse(
            currentPage = page,
            items = results.map { it.toMovieEntity() },
            totalPages = totalPages,
            totalItems = totalResults
        )
    }

    override suspend fun searchForTvShows(
        name: String, language: String, pageNumber: Int
    ): PagedFetchResponse<TvShow> = fetchAndSync(
        cacheBlock = {
        localTvShowDataSource.getByQueryAndPage(
            query = name + language, page = pageNumber
        )
    }, networkBlock = {
        remoteDataSource.searchForTvShows(
            query = name,
            language = language,
            includeAdult = false,
            pageNumber = pageNumber,
        ).toLocal(query = name + language)
    }, syncBlock = { localTvShowDataSource.insert(it) }, crashReporter = crashReporter
    ).run {
        PagedFetchResponse(
            currentPage = page,
            items = results.map { it.toTvShowEntity() },
            totalPages = totalPages,
            totalItems = totalResults
        )
    }

    override suspend fun searchForActors(
        name: String, language: String, pageNumber: Int
    ): PagedFetchResponse<Actor> = fetchAndSync(
        cacheBlock = {
        localActorDataSource.getByQueryAndPage(
            query = name + language, page = pageNumber
        )
    }, networkBlock = {
        remoteDataSource.searchForActors(
            query = name,
            language = language,
            includeAdult = false,
            pageNumber = pageNumber,
        ).toLocal(query = name + language)
    }, syncBlock = { localActorDataSource.insert(it) }, crashReporter = crashReporter
    ).run {
        PagedFetchResponse(
            currentPage = page,
            items = results.map { it.toActorEntity() },
            totalPages = totalPages,
            totalItems = totalResults
        )
    }

    override suspend fun searchForMoviesByCategory(
        categoryId: Int, language: String, pageNumber: Int
    ): PagedFetchResponse<Movie> = fetchAndSync(
        networkBlock = {
            remoteDataSource.getMoviesByCategory(
                categoryId = categoryId,
                language = language,
                pageNumber = pageNumber,
            ).toLocal(query = "")
        }).run {
        PagedFetchResponse(
            currentPage = page,
            items = results.map { it.toMovieEntity() },
            totalPages = totalPages,
            totalItems = totalResults
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